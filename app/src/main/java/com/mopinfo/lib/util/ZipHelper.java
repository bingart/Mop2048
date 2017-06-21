package com.mopinfo.lib.util;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by mop on 2016/8/24.
 */
public class ZipHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(ZipHelper.class);

    public static byte[] zip(byte[] unzippedBuffer)
    {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(unzippedBuffer.length);
            zip.putNextEntry(entry);
            zip.write(unzippedBuffer);
            zip.closeEntry();
            zip.close();
            b = bos.toByteArray();
            bos.close();
        } catch (Exception ex) {
            LOGGER.error("zip error, " + ex.getMessage());
            ex.printStackTrace();
        }
        return b;
    }

    public static byte[] unzip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ZipInputStream zip = new ZipInputStream(bis);
            while (zip.getNextEntry() != null) {
                byte[] buf = new byte[1024];
                int num = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((num = zip.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, num);
                }
                b = baos.toByteArray();
                baos.flush();
                baos.close();
            }
            zip.close();
            bis.close();
        } catch (Exception ex) {
            LOGGER.error("unzip error, " + ex.getMessage());
            ex.printStackTrace();
        }
        return b;
    }
}
