package com.mopinfo.mop2048.config;

import android.content.Context;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mop on 2016/8/25.
 */
public class ConfigManager {

    ILogger LOGGER = LogMannger.getInstance().getLogger(ConfigManager.class);

    public static final String DEFAULT_UID = "U";
    public static final String DEFAULT_APP_VERSION_HOST = "http://www.mopinfo.com/app";
    public static final String DEFAULT_APP_RESOURCE_HOST = "http://www.mopinfo.com/service/resource.php";
    private static final String DEFAULT_MESSAGE_SERVER_HOST = "http://message.mopinfo.com/";

    private Properties mProperties;

    private String uid = DEFAULT_UID;
    private String appVersionHost = DEFAULT_APP_VERSION_HOST;
    private String appResourceHost = DEFAULT_APP_RESOURCE_HOST;
    private int usingMessageServer = 0;
    private String messageServerHost = DEFAULT_MESSAGE_SERVER_HOST;
    private String appKey = null;
    private String channelID = null;

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

        uid = get("uid");
        appVersionHost = get("appVersionHostHost");
        appResourceHost = get("appResourceHost");
        usingMessageServer = getInt("usingMessageServer", 0);
        messageServerHost = get("messageServerHost");
        appKey = get("appKey");
        channelID = get("channelID");
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

    public String getAppVersionHost() {
        return appVersionHost;
    }

    public String getAppResourceHost() {
        return appResourceHost;
    }

    public String getUid() {
        return uid;
    }

    public boolean isUsingMessageServer() { return usingMessageServer == 1; }

    public String getMessageServerHost() { return messageServerHost; }

    public String getAppKey() { return appKey; }

    public String getChannelID() { return channelID; }

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
