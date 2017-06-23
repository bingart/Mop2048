package com.mopinfo.lib.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by feisun on 2017/6/21.
 */
public class FileHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(FileHelper.class);

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Write string to external storage file..
     * @param path
     * @param fileName
     * @param content
     */
    public static void writeFileContent(File path, String fileName, String content) {
        FileOutputStream fos = null;
        try {
            File file = new File(path, fileName);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            byte[] bytes = content.getBytes(Charset.forName("UTF8"));
            fos.write(bytes);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Write string to external storage file..
     * @param filePath
     * @param fileName
     * @param content
     */
    public static void writeFileContent(String filePath, String fileName, String content) {
        FileOutputStream fos = null;
        try {
            String fullPath = String.format("%s/%s", filePath, fileName);
            File file = new File(fullPath);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            byte[] bytes = content.getBytes(Charset.forName("UTF8"));
            fos.write(bytes);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Write string to internal storage file.
     * @param fileName
     * @param content
     */
    public static void writeFileContent(Context context, String fileName, String content) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] bytes = content.getBytes(Charset.forName("UTF8"));
            fos.write(bytes);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read string from external storage file.
     * @param path
     * @param fileName
     * @return
     */
    public static String readFileConent(File path, String fileName) {
        FileInputStream fin = null;
        try {
            File file = new File(path, fileName);
            if (file.exists()) {
                fin = new FileInputStream(file);
                int length = fin.available();
                byte [] buffer = new byte[length];
                fin.read(buffer);
                String str = new String(buffer, 0, length, "UTF-8");
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Read string from external storage file.
     * @param filePath
     * @param fileName
     * @return
     */
    public static String readFileConent(String filePath, String fileName) {
        FileInputStream fin = null;
        try {
            String fullPath = String.format("%s/%s", filePath, fileName);
            File file = new File(fullPath);
            if (file.exists()) {
                fin = new FileInputStream(file);
                int length = fin.available();
                byte [] buffer = new byte[length];
                fin.read(buffer);
                String str = new String(buffer, 0, length, "UTF-8");
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * string from internal storage file.
     * @param context
     * @param fileName
     * @return
     */
    public static String readFileConent(Context context, String fileName) {
        FileInputStream fin = null;
        try {
            fin = context.openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            String str = new String(buffer, 0, length, "UTF-8");
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Check if file exists in external storage.
     * @param path
     * @param fileName
     * @return
     */
    public static boolean isExists(File path, String fileName) {
        File file = new File(path, fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if file exists
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean isExists(String filePath, String fileName) {
        String fullPath = String.format("%s/%s", filePath, fileName);
        File file = new File(fullPath);
        return file.exists();
    }

    /**
     * Check if file exists in internal storage.
     * @param fileName
     * @return
     */
    public static boolean isExists(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public static void deleteOnExit(String filePath, String fileName) {
        String fullPath = String.format("%s/%s", filePath, fileName);
        File file = new File(fullPath);
        file.deleteOnExit();
        LOGGER.info(String.format("File deletedOnExit, fileName=%s", fileName));
    }

    public static void deleteOnExit(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        file.deleteOnExit();
        LOGGER.info(String.format("File deletedOnExit, fileName=%s", fileName));
    }
}
