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
            // Android N1
            "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            // Android Opera Mobile
            "Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10",
            // UC Standard
            "NOKIA5700/ UCWEB7.0.2.37/28/999",
            // UCOpenwave
            "Openwave/ UCWEB7.0.2.37/28/999",
            // UC Opera
            "Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/999",
            // safari iOS 4.33 – iPhone
            "User-Agent,Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
            // safari iOS 4.33 – iPod Touch
            "User-Agent,Mozilla/5.0 (iPod; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
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
