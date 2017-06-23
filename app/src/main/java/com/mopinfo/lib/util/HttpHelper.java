package com.mopinfo.lib.util;

import android.content.Context;

import com.mopinfo.lib.common.ErrorCode;
import com.mopinfo.lib.common.NutchException;
import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(HttpHelper.class);

    /**
     * HTTP GET.
     * @param url
     * @param clazz
     * @param <TRSP>
     * @return instance of TRSP
     * @throws IOException
     * @throws NutchException
     */
    public static <TRSP> TRSP get(String url, Class<TRSP> clazz) throws IOException, NutchException {
        TRSP response = null;
        Response okResponse = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request okRequest = new Request.Builder().url(url).get().build();
            okResponse = client.newCall(okRequest).execute();
            if (okResponse != null && okResponse.isSuccessful()) {
                String rspContentString = okResponse.body().string();
                response = JsonHelper.<TRSP>fromJson(rspContentString, clazz);
                if (response != null) {
                    // LOGGER.debug("ok");
                }
            } else {
                if (okResponse != null) {
                    LOGGER.error("HttpHelper: get error, status code is " + okResponse.code());
                } else {
                    LOGGER.error("HttpHelper: get error, response is null");
                }
            }
        } catch (Exception ex) {
            LOGGER.error("HttpHelper: get error, message=" + ex.getMessage());
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

    /**
     * HTTP POST.
     * @param url
     * @param request
     * @param clazz
     * @param isCrypt
     * @param <TREQ>
     * @param <TRSP>
     * @return instance of TRSP
     * @throws IOException
     * @throws NutchException
     */
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

    /**
     * Download server file into local external storage file.
     * @param serverAppUrl
     * @param serverFileName
     * @param localFilePath
     * @param localFileName
     * @return length of downloaded file if ok
     * @throws Exception
     */
    public static long download(String serverAppUrl, String serverFileName, String localFilePath, String localFileName) throws Exception {
        try {
            int serverVersionCode = 0;
            OkHttpClient client = new OkHttpClient();
            String url = null;
            if (serverAppUrl.endsWith("/")) {
                url = serverAppUrl + serverFileName;
            } else {
                url = serverAppUrl + "/" + serverFileName;
            }
            Request request = new Request.Builder().url(url).build();
            LOGGER.debug(String.format("Download, url=%s, wait ...", url));

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {
                // Read file
                InputStream is = null;
                byte[] buffer = new byte[64];
                int len;
                long totalLen = 0L;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    String filePath = String.format("%s/%s", localFilePath, localFileName);
                    File file = new File(filePath);
                    if (file.exists()) {
                        // If file exists, delete it
                        file.delete();
                    } else {
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        totalLen += len;
                    }
                    fos.flush();
                    LOGGER.info(String.format("Download ok, localFileName=%s, size=%d", localFileName, totalLen));
                    return totalLen;
                } catch (IOException e) {
                    LOGGER.error(String.format("Download error, localFileName=%s, ex=%s", localFileName, e.getMessage()));
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new Exception();
        }

        return 0;
    }
}

