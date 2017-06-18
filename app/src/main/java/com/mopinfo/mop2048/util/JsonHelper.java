package com.mopinfo.mop2048.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;

import java.io.IOException;

/**
 * Created by mop on 2016/8/24.
 */
public class JsonHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(JsonHelper.class);

    public static <T> String toJson (T obj) {
        String res = null;
        if (obj != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            try {
                LOGGER.debug("toJson");
                res = mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        T t = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            LOGGER.debug("fromJson, json=" + json);
            t = mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("fromJson error, " + e.getMessage());
            e.printStackTrace();
        }
        return t;
    }
}
