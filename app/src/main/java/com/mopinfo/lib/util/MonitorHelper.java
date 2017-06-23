package com.mopinfo.lib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by feisun on 2017/6/23.
 */
public class MonitorHelper {

    /**
     * Check if network is available
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(null == connMgr){
            return false;
        }
        NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifiInfo != null && wifiInfo.isAvailable()){
            return true;
        }else if(mobileInfo != null && mobileInfo.isAvailable()){
            return true;
        }else{
            return false;
        }
    }
}
