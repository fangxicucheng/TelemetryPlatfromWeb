package com.fang.utils;

import jdk.jfr.Unsigned;
import org.apache.commons.compress.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UTCUtils {

    public static Date getUTC8PLUSTime(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 1, 1, 20, 0, 0);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static String getUTC8PLUSTimeStr(double seconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date date = getUTC8PLUSTime((int) seconds);
        return simpleDateFormat.format(date);
    }


}
