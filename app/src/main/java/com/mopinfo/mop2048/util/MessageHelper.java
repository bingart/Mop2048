package com.mopinfo.mop2048.util;

import com.mopinfo.mop2048.common.ErrorCode;
import com.mopinfo.mop2048.common.NutchException;
import com.mopinfo.mop2048.json.message.MessageRequest;
import com.mopinfo.mop2048.json.message.MessageResponse;
import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;

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
