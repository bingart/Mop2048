package com.mopinfo.mop2048.util;

import com.mopinfo.mop2048.common.ErrorCode;
import com.mopinfo.mop2048.common.NutchException;
import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HttpHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(HttpHelper.class);

    public static <TREQ, TRSP> TRSP postRequest(String url, TREQ request, Class<TRSP> clazz, boolean isCrypt) throws IOException, NutchException {

        TRSP response = null;

        String reqContentString = null;
        if (isCrypt) {
            reqContentString = CryptHelper.<TREQ>encode(request);
        } else {
            reqContentString = JsonHelper.<TREQ>toJson(request);
        }

        Response okResponse = null;
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, reqContentString);
            Request okRequest = new Request.Builder().url(url).post(body).build();
            okResponse = client.newCall(okRequest).execute();
            if (okResponse != null && okResponse.isSuccessful()) {
                String rspContentString = okResponse.body().string();
                response = JsonHelper.<TRSP>fromJson(rspContentString, clazz);
                if (response != null) {
                    // LOGGER.debug("ok");
                }
            } else {
                if (okResponse != null) {
                    LOGGER.error("HttpHelper: postRequest error, status code is " + okResponse.code());
                } else {
                    LOGGER.error("HttpHelper: postRequest error, response is null");
                }
            }
        } catch (Exception ex) {
            LOGGER.error("HttpHelper: postRequest error, message=" + ex.getMessage());
        } finally {
            if (okResponse != null) {
                try {
                    if (okResponse.body() != null) {
                        okResponse.body().close();
                    }
                } catch (Exception ex){
                    throw new NutchException(ErrorCode.QUERY_ERROR, ex.getMessage());
                }
            }
        }

        return response;
    }

    /*
    public static <TRSP> TRSP postRequestWithMap(String url, HashMap<String, String> dict, Class<TRSP> clazz) throws IOException, NutchException {

        TRSP response = null;

        Response okResponse = null;
        try {
            OkHttpClient client = new OkHttpClient();

            // Body
            FormEncodingBuilder feb = new FormEncodingBuilder();
            for (Map.Entry<String, String> e : dict.entrySet()) {
                feb.add(e.getKey(), e.getValue());
            }
            RequestBody body = feb.build();

            // Request
            Request okRequest = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            // Execute
            okResponse = client.newCall(okRequest).execute();
            if (okResponse != null && okResponse.isSuccessful()) {
                String rspContentString = okResponse.body().string();
                response = JsonHelper.<TRSP>fromJson(rspContentString, clazz);
            } else {
                if (okResponse != null) {
                    LOGGER.error("HttpHelper: postRequest error, status code is " + okResponse.code());
                } else {
                    LOGGER.error("HttpHelper: postRequest error, response is null");
                }
            }
        } catch (Exception ex) {
            LOGGER.error("HttpHelper: postRequest error, message=" + ex.getMessage());
        } finally {
            if (okResponse != null) {
                try {
                    if (okResponse.body() != null) {
                        okResponse.body().close();
                    }
                } catch (Exception ex){
                    throw new NutchException(ErrorCode.QUERY_ERROR, ex.getMessage());
                }
            }
        }

        return response;
    }
    */
}
