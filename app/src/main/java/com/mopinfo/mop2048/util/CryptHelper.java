package com.mopinfo.mop2048.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by mop on 2016/8/24.
 */
public class CryptHelper {

    public static final byte KEY = 0x12;
    public static final int CRYPT_SIZE = 10;

    public static <T> T decode(String content, Class<T> clazz) {

        T t = null;

        try
        {
            // From Base64
            //byte[] buffer = org.apache.commons.codec.binary.Base64.decodeBase64(content);
            byte[] buffer = Base64Helper.decode(content);

            // Change bytes
            for (int i = 0; i < CRYPT_SIZE; i++)
            {
                buffer[i] = (byte)(buffer[i] ^ KEY);
            }

            // Unzip
            byte[] decryptBuffer = ZipHelper.unzip(buffer);

            // To string
            String jsonString = new String(decryptBuffer, "UTF-8");

            // Deserialize
            t = JsonHelper.<T>fromJson(jsonString, clazz);
        }
        catch (Exception ex)
        {

        }

        return t;
    }

    public static <T> String encode(T t) throws UnsupportedEncodingException {
        String content = null;

        // Serialize
        String jsonString = JsonHelper.<T>toJson(t);

        // To byte[]
        byte[] decryptBuffer = jsonString.getBytes("UTF-8");

        // Zip
        byte[] cryptBuffer = ZipHelper.zip(decryptBuffer);

        // Change bytes
        for (int i = 0; i < CRYPT_SIZE; i++)
        {
            cryptBuffer[i] = (byte)(cryptBuffer[i] ^ KEY);
        }

        // To Base64
        //content = org.apache.commons.codec.binary.Base64.encodeBase64String(cryptBuffer);
        content = new String(Base64Helper.encode(cryptBuffer));

        return content;
    }
}
