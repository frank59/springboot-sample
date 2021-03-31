package com.geewaza.code.study.camel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lottery
 * @ClassName DateUtils
 * @desc 日期工具类
 * @Date 2018/11/3 10:17
 * @Version 1.0
 **/
public class DateUtils {

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY_MMM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String YYYY_MMM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String ES_YYYY_MMM_DD_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";

    public static final SimpleDateFormat SDF_YYYYDDMM = new SimpleDateFormat(YYYYMMDD);

    public static final SimpleDateFormat SDF_YYYY_MMM_DD_HH_MM_SS = new SimpleDateFormat(YYYY_MMM_DD_HH_MM_SS);

    public static String getNowTime() {
        return SDF_YYYY_MMM_DD_HH_MM_SS.format(System.currentTimeMillis());
    }

    public static String getYMDDateStr() {
        return SDF_YYYYDDMM.format(System.currentTimeMillis());
    }

    public static List<String> getDateRangeList(int interval) {
        List<String> dataRangeList = new LinkedList<String>();
        int step = 24 / interval;
        for (int i = 0; i < interval; i++) {
            dataRangeList.add((i * step) + "_" + (i + 1) * step);
        }
        return dataRangeList;
    }

    /**
     * 获取
     * @param interval
     * @return
     */
    public static String getNowDateRange(int interval) {
        Map<Integer, String> dataRangeMap = new HashMap<Integer, String>();
        int step = 24 / interval;
        for (int i = 0; i < 24; i++) {
            int start = i / step;
            dataRangeMap.put(i, (start * step) + "_" + (start + 1) * step);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return dataRangeMap.get(hours);
    }

    public static String getCurrentTime() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static Date getZeroDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static String format(String dateFormat, long date) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static Date parse(String dateFormat, String date) {
        try {
            return new SimpleDateFormat(dateFormat).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date moveDate(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    interface DateFormat {
        public static final String DAY = YYYY_MM_DD;

        public static final String MIN = YYYY_MMM_DD_HH_MM;

        public static final String SS = YYYY_MMM_DD_HH_MM_SS;
    }
}