package com.huasit.ssm.system.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     *
     */
    public static boolean isTodayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date()).equals(sdf.format(date));
    }

    /**
     *
     */
    public static boolean isAfterTodayDate(Date date) {
        Date today = getTodayDate();
        return today != null && !date.before(today);
    }

    /**
     *
     */
    public static Date getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     *
     */
    public static String getDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}