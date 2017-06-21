package com.mopinfo.lib.util;

import com.mopinfo.lib.common.ErrorCode;
import com.mopinfo.lib.common.NutchException;
import com.mopinfo.lib.json.message.MessageRequest;
import com.mopinfo.lib.json.message.MessageResponse;
import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;

/**
 * Created by mop on 2016/8/25.
 */
public class MessageHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(MessageHelper.class);

    public static MessageResponse doMessage(String messageServerHost, String uid, String level, String message) throws NutchException {
        String host = messageServerHost;
        if (!host.endsWith("/")) { host += "/"; }

        MessageResponse response = null;

        try {
            MessageRequest request = new MessageRequest();
            request.setUid(uid);
            request.setLevel(level);
            request.setMessage(message);
            String url = host + "WebApi/Message";
            response = HttpHelper.<MessageRequest,MessageResponse>postRequest(
                            url,
                            request,
                            MessageResponse.class,
                            false);
        } catch (Exception ex) {
            LOGGER.error("doMessage error, " + ex.getMessage());
            throw new NutchException(ErrorCode.MESSAGE_ERROR, ex.getMessage());
        }

        return response;
    }
}
