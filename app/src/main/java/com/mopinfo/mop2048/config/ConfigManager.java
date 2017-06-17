package com.mopinfo.mop2048.config;

import android.content.Context;

import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mop on 2016/8/25.
 */
public class ConfigManager {

    ILogger LOGGER = LogMannger.getInstance().getLogger(ConfigManager.class);

    public static final String DEFAULT_HOST = "http://www.mopinfo.com/service/resource";
    public static final String DEFAULT_UID = "U";
    private static final String DEFAULT_MESSAGE_SERVER_HOST = "http://message.mopinfo.com/";

    private Properties mProperties;

    private String host = DEFAULT_HOST;
    private String uid = DEFAULT_UID;
    private int usingMessageServer = 0;
    private String messageServerHost = DEFAULT_MESSAGE_SERVER_HOST;

    private static ConfigManager s;

    /*
    private static class SingletonHolder {
        public final static ConfigManager instance = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        return SingletonHolder.instance;
    }
    */
    public static ConfigManager getInstance() {
        if (s == null)
            s = new ConfigManager();
        return s;
    }

    private ConfigManager() {}

    public void load(Context context) {
        mProperties = getProperties(context);

        host = get("host");
        uid = get("uid");
        usingMessageServer = getInt("usingMessageServer", 0);
        messageServerHost = get("messageServerHost");
    }

    public Properties getProperties(Context c){
        Properties props = new Properties();
        try {
            //方法一：通过activity中的context攻取setting.properties的FileInputStream
            //InputStream in = c.getAssets().open("appConfig.properties");
            //方法二：通过class获取setting.properties的FileInputStream
            InputStream in = this.getClass().getResourceAsStream("/assets/appConfig.properties");
            props.load(in);
        } catch (Exception ex) {
            LOGGER.error("getProperties error " + ex.getMessage());
            return null;
        }

        return props;
    }

    public String getHost() {
        return host;
    }

    public String getUid() {
        return uid;
    }

    public boolean isUsingMessageServer() { return usingMessageServer == 1; }

    public String getMessageServerHost() { return messageServerHost; }

    private String get(String key) {
        return mProperties.getProperty(key);
    }

    private int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value != null) {
            return Integer.parseInt(value);
        } else {
            return defaultValue;
        }
    }
}
