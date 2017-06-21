package com.mopinfo.lib.logic;

import android.content.pm.PackageInfo;
import android.os.Environment;
import android.util.Log;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.mopinfo.lib.util.FileHelper;
import com.mopinfo.lib.util.HttpHelper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Download version file and apk file.
 * Created by feisun on 2017/1/2.
 */
public class UpdateManager {

    ILogger LOGGER = LogMannger.getInstance().getLogger(UpdateManager.class);

    private static UpdateManager s;

    private String mServerAppUrl;
    private String mVersionFileName;
    private String mLocalApkFileName;
    private String mServerApkFileName;

    private boolean mRunning;

    public static UpdateManager getInstance() {
        if (s == null)
            s = new UpdateManager();
        return s;
    }

    private UpdateManager() {
        mServerAppUrl = null;
        mVersionFileName = null;
        mLocalApkFileName = null;
        mServerApkFileName = null;
        mRunning = false;
    }

    public boolean isNewVersionFound(PackageInfo packageInfo) {
        try {
            String currentVersionStr = packageInfo.versionName;
            int currentVesion = Integer.parseInt(currentVersionStr);
            String serverVersionStr = FileHelper.readFileConent(
                    Environment.getExternalStorageDirectory(),
                    mVersionFileName);
            int serverVesion = Integer.parseInt(serverVersionStr);
            if (serverVesion > currentVesion) {
                return true;
            }
        } catch (Exception ex) {
            Log.e("nutch", "isNewVersionFound error, ex=" + ex);
        }
        return false;
    }

    public synchronized void start(
            String serverAppUrl,
            String versionFileName,
            String localApkFileName,
            String serverApkFileName) {
        if (mRunning) {
            return;
        } else {
            mRunning = true;
        }

        mServerAppUrl = serverAppUrl;
        mVersionFileName = versionFileName;
        mLocalApkFileName = localApkFileName;
        mServerApkFileName = serverApkFileName;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    HttpHelper.download(
                            mServerAppUrl, mServerApkFileName,
                            Environment.getExternalStorageDirectory(), mLocalApkFileName);
                    HttpHelper.download(
                            mServerAppUrl, mVersionFileName,
                            Environment.getExternalStorageDirectory(), mVersionFileName);
                } catch (Exception ex) {
                    Log.e("nutch", "update error, ex=" + ex.getMessage());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
