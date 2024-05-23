package com.fang.utils;

import jdk.jfr.Unsigned;
import org.apache.commons.compress.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class UTCUtils {

    //    public static Date getUTC8PLUSTime(int seconds) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2000, 1, 1, 20, 0, 0);
//        calendar.add(Calendar.SECOND, seconds);
//        return calendar.getTime();
//    }
    public static LocalDateTime getUTCTime(double seconds) {
        LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 20, 0, 0);

        return localDateTime.plusSeconds((long) seconds);
    }

    public static String getUTCDirectory(){

        LocalDateTime now = LocalDateTime.now();
        return now.format(dateFormatter);
    }
    public static String getUTCStr(){
        LocalDateTime now=LocalDateTime.now();
        return now.format(dateTimeStrFormatter);
    }

    public static DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static DateTimeFormatter dateTimeStrFormatter=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String getUTCTimeStr(double seconds) {

        LocalDateTime dateTime = getUTCTime(seconds);
        return convertLocalTimeToStr(dateTime);

    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();

        // 使用时区信息将 Instant 转换为 LocalDateTime
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        // 将 LocalDateTime 转换为 Instant
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // 将 Instant 转换为 Date
        return Date.from(instant);
    }

    public static String convertLocalTimeToStr(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);

    }
    public static String convertLocalTimeToStrFilePath(LocalDateTime dateTime){
        return dateTime.format(dateTimeStrFormatter);
    }


}
