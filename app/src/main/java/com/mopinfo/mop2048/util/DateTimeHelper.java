package com.mopinfo.mop2048.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mop on 2016/8/29.
 */
public class DateTimeHelper {

    public static String getCurrentDateTimeString() {

        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        StringBuilder b = new StringBuilder();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date()).toString();
    }
}
