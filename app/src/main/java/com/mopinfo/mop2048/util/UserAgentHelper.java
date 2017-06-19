package com.mopinfo.mop2048.util;

/**
 * Created by feisun on 2017/6/19.
 */
public class UserAgentHelper {

    private static String[] USER_AGENT_LIST = new String[] {
            // QQBrowser on Android
            "MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            // Chrome on PC
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
            // Chrome on Mobile
            "User-Agent: Mozilla/5.0 (Linux; U; Android 2.2.1; zh-cn; HTC_Wildfire_A3333 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
    };

    public static String getUserAgent(String uid) {
        byte[] bytes = uid.getBytes();
        int total = 0;
        for (byte b : bytes) {
            total += (int) b;
        }

        int index = total % USER_AGENT_LIST.length;
        return USER_AGENT_LIST[index];
    }
}
