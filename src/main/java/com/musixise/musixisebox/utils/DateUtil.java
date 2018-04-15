package com.musixise.musixisebox.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class DateUtil {

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmssSSS";

    public static String asDate(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            Date dd = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dd);
            return dateStr;
        }

        return "";
    }

    public static String asDate(Date date) {
        try {
            String format = "yyyy-MM-dd HH:mm:ss";
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String asDate(String format, Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static LocalDateTime asLocalDateTime(String date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(date, formatter);
        } else {
            return null;
        }
    }

    public static String getTimeFormatText(Long date) {
        if (date == null) {
            return null;
        }
        long diff = System.currentTimeMillis() - date;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将时间戳转换成当天0点
     * @param timestamp
     * @return
     */
    public static long getDayBegin(long timestamp) {
        String format = "yyyy-MM-DD";
        String toDayString = new SimpleDateFormat(format).format(new Date(timestamp));
        Date toDay = null;
        try {
            toDay = org.apache.commons.lang.time.DateUtils.parseDate(toDayString, new String[]{format});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toDay.getTime();
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取一个月之前的时间戳
     * @return
     */
    public static long getLastMonthTime() {
        return getDayBegin(getCurrentTime())- 86400000L *30;
    }


}
