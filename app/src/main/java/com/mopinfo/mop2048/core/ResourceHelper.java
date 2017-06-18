package com.mopinfo.mop2048.core;

import com.mopinfo.mop2048.common.ErrorCode;
import com.mopinfo.mop2048.common.NutchException;
import com.mopinfo.mop2048.json.url.UrlRequest;
import com.mopinfo.mop2048.json.url.UrlResponse;
import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;
import com.mopinfo.mop2048.util.HttpHelper;

/**
 * Created by mop on 2016/8/25.
 */
public class ResourceHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(ResourceHelper.class);

    public static UrlResponse getResource(String urlHost, String uid) throws NutchException {
        try {
            String getUrl = urlHost + "?uid=" + uid;
            UrlResponse response = HttpHelper.<UrlResponse>get(getUrl, UrlResponse.class);
            return response;
        } catch (Exception ex) {
            LOGGER.error("getUrl error, " + ex.getMessage());
            throw new NutchException(ErrorCode.QUERY_ERROR, ex.getMessage());
        }
    }

    public static UrlResponse postResource(String urlHost, String uid) throws NutchException {

        try {
            UrlRequest request = new UrlRequest();
            request.setUid(uid);
            UrlResponse response = HttpHelper.<UrlRequest, UrlResponse>postRequest(urlHost, request, UrlResponse.class, false);
            return response;
        } catch (Exception ex) {
            LOGGER.error("getUrl error, " + ex.getMessage());
            throw new NutchException(ErrorCode.QUERY_ERROR, ex.getMessage());
        }
    }
}
