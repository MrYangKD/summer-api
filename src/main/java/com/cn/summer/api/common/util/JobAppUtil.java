package com.cn.summer.api.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
public class JobAppUtil {


    private static final String SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private JobAppUtil() {
    }
    /**
     * 判断字符串不为NULL,null,空字符串方法.
     * @param str str
     * @return boolean
     */
    public static boolean isNotNull(final String str) {
        return str != null && !"".equals(str) && !"null".equals(str) && str.length() > 0;
    }

    /**
     * 判断字符串为NULL, null, 空字符串方法.
     * @param str str
     * @return boolean
     */
    public static boolean isNull(final String str) {
        return !isNotNull(str);
    }

    /**
     * 判断date不为NULL,null,空字符串方法.
     * @param date date
     * @return boolean
     */
    @SuppressWarnings("unlikely-arg-type")
    public static boolean isNull(final Date date) {
        return date != null && !"".equals(date) && !"null".equals(date);
    }

    /**
     * Date转换为String类型.
     * @param date date
     * @return String
     */
    public static String convertDateToString(final Date date){

        DateFormat df = new SimpleDateFormat(SSS);
        String dateStr=df.format(date);
        return dateStr;
    }

    /**
     * 根据传入日期获取目标日期（天）.
     * @param date 原日期
     * @param day （day小于0则日期往前）
     * @return Date
     */
    public static Date targetDay(final Date date, final int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
        return calendar.getTime();
    }

    /**
     * 根据日期字符串转换为时间戳.
     * @param time String
     * @return long时间戳
     * @throws Exception e
     */
    public static long dateToTime(final String time) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(SSS);
        Date date = sdf.parse(time);
        return date.getTime();
    }

}
