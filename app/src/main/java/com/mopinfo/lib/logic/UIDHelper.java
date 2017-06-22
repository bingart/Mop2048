package com.mopinfo.lib.logic;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.mopinfo.lib.util.DateTimeHelper;

/**
 * Created by feisun on 2017/6/22.
 */
public class UIDHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(UIDHelper.class);

    public static String getUID(Context context, String defaultUID) {
        try {
            TelephonyManager t = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            String imei = t.getDeviceId();
            if (imei != null) {
                return defaultUID + imei;
            }
        } catch (Exception ex) {
            LOGGER.error("get imei error, " + ex.getMessage());
        }

        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String mac = info.getMacAddress();
            if (mac != null) {
                return defaultUID + mac;
            }
        } catch (Exception ex) {
            LOGGER.error("get mac address error, " + ex.getMessage());
        }

        return defaultUID + DateTimeHelper.getCurrentDateTimeString();
    }
}
