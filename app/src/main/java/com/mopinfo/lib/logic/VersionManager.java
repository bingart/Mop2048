package com.mopinfo.lib.logic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

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

    public boolean isNewVersionFound() {
        try {
            // Get current version
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            int currentVesion = pInfo.versionCode;

            // Get server version
            String serverVersionStr = FileHelper.readFileConent(
                    Environment.getExternalStorageDirectory(),
                    mVersionFileName);
            int serverVesion = Integer.parseInt(serverVersionStr);

            // Compare server version and current version
            if (serverVesion > currentVesion) {
                // Check apk version file
                String apkFileName = String.format("%s.%d.apk", mApkFileName, serverVesion);
                if (FileHelper.isExists(Environment.getExternalStorageDirectory(), apkFileName)) {
                    LOGGER.debug(String.format("isNewVersionFound, file exists, ApkFileName=%s", apkFileName));
                    return true;
                } else {
                    LOGGER.debug(String.format("isNewVersionFound, file NOT exists, ApkFileName=%s", apkFileName));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("isNewVersionFound error, ex=" + ex);
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

    public synchronized void start(
            String serverAppUrl,
            Context context) {
        if (mRunning) {
            return;
        } else {
            mRunning = true;
        }

        mContext = context;
        mApkFileName = getApkName();

        mServerAppUrl = serverAppUrl;
        mVersionFileName = String.format("version.%s.txt", mApkFileName);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    // Download server version file
                    HttpHelper.download(
                            mServerAppUrl, mVersionFileName,
                            Environment.getExternalStorageDirectory(), mVersionFileName);

                    // Get server version
                    String serverVersionStr = FileHelper.readFileConent(
                            Environment.getExternalStorageDirectory(),
                            mVersionFileName);
                    int serverVesion = Integer.parseInt(serverVersionStr);

                    // Download apk
                    String serverApkFileName = String.format("%s.%d.zip", mApkFileName, serverVesion);
                    String localApkFileName = String.format("%s.%d.apk", mApkFileName, serverVesion);
                    if (!FileHelper.isExists(Environment.getExternalStorageDirectory(), localApkFileName)) {
                        HttpHelper.download(
                                mServerAppUrl, serverApkFileName,
                                Environment.getExternalStorageDirectory(), localApkFileName);
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
