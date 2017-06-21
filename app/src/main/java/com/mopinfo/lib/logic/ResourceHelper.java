package com.mopinfo.lib.logic;

import com.mopinfo.lib.common.ErrorCode;
import com.mopinfo.lib.common.NutchException;
import com.mopinfo.lib.json.resource.UrlRequest;
import com.mopinfo.lib.json.resource.UrlResponse;
import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.mopinfo.lib.util.HttpHelper;

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
