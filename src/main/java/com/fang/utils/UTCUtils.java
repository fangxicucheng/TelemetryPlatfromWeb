package com.fang.utils;

import jdk.jfr.Unsigned;
import org.apache.commons.compress.utils.TimeUtils;

import java.util.Calendar;
import java.util.Date;

public class UTCUtils {

   public static Date getUTC8PLUSTime(int seconds){
       Calendar calendar=Calendar.getInstance();
       calendar.set(2000,1,1,20,0,0);
       calendar.add(Calendar.SECOND,seconds);
       return calendar.getTime();
   }


}
