package com.mopinfo.lib.logic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.mopinfo.lib.util.FileHelper;
import com.mopinfo.lib.util.HttpHelper;

import java.io.File;

/**
 * Download version file and apk file.
 * Created by feisun on 2017/1/2.
 */
public class VersionManager {

    ILogger LOGGER = LogMannger.getInstance().getLogger(VersionManager.class);

    private static VersionManager s;

    private Context mContext;

    private String mServerAppUrl;
    private String mFilePath;
    private String mVersionFileName;
    private String mApkFileName;
    private String mApkVersionFileName;

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
        mApkVersionFileName = null;
        mRunning = false;
    }

    public void open(String serverAppUrl, Context context) {
        mContext = context;
        mServerAppUrl = serverAppUrl;
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        // mFilePath = mContext.getFilesDir().getAbsolutePath();
        String apkName = getApkName();
        mVersionFileName = String.format("version.%s.txt", apkName);
        mApkFileName = String.format("%s.apk", apkName);
        mApkVersionFileName = null;
        LOGGER.info(String.format("open, mApkFileName=%s, mVersionFileName=%s", mApkFileName, mVersionFileName));
    }

    public boolean isNewVersionFound() {
        // Get current version
        int currentVesion = getVersionCode();

        int serverVesion = 0;
        try {
            // Get server version
            String serverVersionStr = FileHelper.readFileConent(mFilePath, mVersionFileName);
            if (serverVersionStr != null) {
                serverVesion = Integer.parseInt(serverVersionStr);
                LOGGER.info(String.format("isNewVersionFound, serverVersion=%d", serverVesion));
            } else {
                LOGGER.debug(String.format("isNewVersionFound, serverVersionStr is null"));
            }
        } catch (Exception ex) {
            LOGGER.error("isNewVersionFound error, ex=" + ex);
        }

        // Compare server version and current version
        if (serverVesion > currentVesion) {
            // Check apk version file
            mApkVersionFileName = mApkFileName.replaceAll(".apk", String.format(".%d.apk", serverVesion));
            if (FileHelper.isExists(mFilePath, mApkVersionFileName)) {
                LOGGER.debug(String.format("isNewVersionFound, file exists, mApkVersionFileName=%s", mApkVersionFileName));
                notifyAndInstall(mFilePath, mApkVersionFileName);
                return true;
            } else {
                LOGGER.debug(String.format("isNewVersionFound, file NOT exists, mApkVersionFileName=%s", mApkVersionFileName));
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
                            mServerAppUrl, mVersionFileName, mFilePath, mVersionFileName);
                    LOGGER.info(String.format(
                            "Download server version file ok, mServerAppUrl=%s, mVersionFileName=%s",
                            mServerAppUrl, mVersionFileName));

                    // Get server version
                    String serverVersionStr = FileHelper.readFileConent(mFilePath, mVersionFileName);
                    LOGGER.info(String.format(
                            "Get server version ok, serverVersionStr=%s",
                            serverVersionStr));
                    int serverVesion = Integer.parseInt(serverVersionStr);
                    LOGGER.info(String.format(
                            "Get server version ok, serverVesion=%d",
                            serverVesion));

                    // Download apk
                    mApkVersionFileName = mApkFileName.replaceAll(".apk", String.format(".%s.apk", serverVesion));
                    if (!FileHelper.isExists(mFilePath, mApkVersionFileName)) {
                        long length = HttpHelper.download(
                                mServerAppUrl, mApkVersionFileName,
                                mFilePath, mApkVersionFileName);
                        LOGGER.info(String.format(
                                "Download server apk file ok, mApkVersionFileName=%s, length=%d",
                                mApkVersionFileName, length));
                    } else {
                        LOGGER.error(String.format("Download ignored, local apk file exists, mApkVersionFileName=%s", mApkVersionFileName));
                    }
                } catch (Exception ex) {
                    LOGGER.error("Download error, ex=" + ex.getStackTrace());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    /**
     * The new version notification dialog.
     */
    private void notifyAndInstall(final String apkFilePath, final String apkFileName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("发现新版本，是否更新?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = new File(apkFilePath, apkFileName);
                if (!file.exists()) {
                    LOGGER.error("notifyAndInstall error, apk file NOT found, path=" + apkFileName);
                    return;
                } else {
                    int length = (int) file.length();
                    LOGGER.info(String.format("notifyAndInstall ok, apk file path=%s, length=%d", apkFileName, length));
                }

                // Schedule file to be deleted on exit
                FileHelper.deleteOnExit(apkFilePath, apkFileName);

                // Update
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete it
                FileHelper.delete(mFilePath, mApkFileName);

                // Close dialog
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
