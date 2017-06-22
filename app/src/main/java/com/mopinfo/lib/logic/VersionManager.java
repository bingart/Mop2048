package com.mopinfo.lib.logic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.mopinfo.lib.util.FileHelper;
import com.mopinfo.lib.util.HttpHelper;

/**
 * Download version file and apk file.
 * Created by feisun on 2017/1/2.
 */
public class VersionManager {

    ILogger LOGGER = LogMannger.getInstance().getLogger(VersionManager.class);

    private static VersionManager s;

    private Context mContext;

    private String mServerAppUrl;
    private String mVersionFileName;
    private String mApkFileName;

    private boolean mRunning;

    public static VersionManager getInstance() {
        if (s == null)
            s = new VersionManager();
        return s;
    }

    private VersionManager() {
        mServerAppUrl = null;
        mVersionFileName = null;
        mApkFileName = null;
        mRunning = false;
    }

    public void open(String serverAppUrl, Context context) {
        mContext = context;
        mApkFileName = getApkName();
        mServerAppUrl = serverAppUrl;
        mVersionFileName = String.format("version.%s.txt", mApkFileName);
    }

    public boolean isNewVersionFound() {
        // Get current version
        int currentVesion = getVersionCode();

        int serverVesion = 0;
        try {
            // Get server version
            String serverVersionStr = FileHelper.readFileConentInternal(
                    mContext,
                    mVersionFileName);
            if (serverVersionStr != null) {
                serverVesion = Integer.parseInt(serverVersionStr);
                LOGGER.debug(String.format("isNewVersionFound, serverVersion=%d", serverVesion));
            } else {
                LOGGER.debug(String.format("isNewVersionFound, serverVersionStr is null"));
            }
        } catch (Exception ex) {
            LOGGER.error("isNewVersionFound error, ex=" + ex);
        }

        // Compare server version and current version
        if (serverVesion > currentVesion) {
            // Check apk version file
            String apkFileName = String.format("%s.%d.apk", mApkFileName, serverVesion);
            if (FileHelper.isExistsInternal(mContext, apkFileName)) {
                LOGGER.debug(String.format("isNewVersionFound, file exists, ApkFileName=%s", apkFileName));
                return true;
            } else {
                LOGGER.debug(String.format("isNewVersionFound, file NOT exists, ApkFileName=%s", apkFileName));
            }
        } else {
            LOGGER.debug(String.format(
                    "isNewVersionFound, ignored, serverVesion=%d, currentVesion=%d", serverVesion, currentVesion));
        }

        return false;
    }

    /**
     * Get the apk path of this application.
     * @return full apk file path, or null if an exception happened (it should not happen)
     */
    public String getApkName() {
        String packageName = mContext.getPackageName();
        PackageManager pm = mContext.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            CharSequence cs = pm.getApplicationLabel(ai);
            String apkName = cs.toString();
            LOGGER.debug(String.format("getApkName=%s", apkName));
            return apkName;
        } catch (Throwable x) {
        }
        return null;
    }

    public int getVersionCode() {
        try {
            String packageName = mContext.getPackageName();
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(packageName, 0);
            int currentVesion = pInfo.versionCode;
            return currentVesion;
        } catch (Exception ex) {
            LOGGER.error("getVersionCode error, ex=" + ex);
            return 10000;
        }
    }

    public synchronized void start() {
        if (mRunning) {
            return;
        } else {
            mRunning = true;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    // Download server version file
                    HttpHelper.download(
                            mServerAppUrl, mVersionFileName,
                            mContext, null, mVersionFileName);
                    LOGGER.debug(String.format(
                            "Download server version file ok, mServerAppUrl=%s, mVersionFileName=%s",
                            mServerAppUrl, mVersionFileName));

                    // Get server version
                    String serverVersionStr = FileHelper.readFileConentInternal(mContext, mVersionFileName);
                    int serverVesion = Integer.parseInt(serverVersionStr);
                    LOGGER.debug(String.format(
                            "Get server version ok, serverVesion=%d",
                            serverVesion));

                    // Download apk
                    String serverApkFileName = String.format("%s.%d.zip", mApkFileName, serverVesion);
                    String localApkFileName = String.format("%s.%d.apk", mApkFileName, serverVesion);
                    if (!FileHelper.isExistsInternal(mContext, localApkFileName)) {
                        HttpHelper.download(
                                mServerAppUrl, serverApkFileName,
                                mContext, null, localApkFileName);
                        LOGGER.debug(String.format(
                                "Download server apk file ok, serverApkFileName=%s, localApkFileName=%s",
                                serverApkFileName, localApkFileName));
                    } else {
                        LOGGER.debug(String.format("Download ignored, local apk file exists, localApkFileName=%s", localApkFileName));
                    }
                } catch (Exception ex) {
                    LOGGER.error("Download error, ex=" + ex.getMessage());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
