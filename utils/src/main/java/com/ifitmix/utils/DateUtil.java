package com.ifitmix.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 */
public class DateUtil {

    /**
     * 一天转化为小时。单位：小时
     */
    public static final int DAY_2_HOUR = 24;
    public static final int WEEK_2_DAY = 7;
    public static final long SECOND_LONG = 1000L;
    public static final long MINUTE_LONG = 60 * SECOND_LONG;
    public static final long HOUR_LONG = 60 * MINUTE_LONG;
    public static final long DAY_LONG = DAY_2_HOUR * HOUR_LONG;
    public static final long MONTH_LONG = 30 * DAY_LONG;
    public static final long YEAR_LONG = 365 * DAY_LONG;
    public static final String DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT_MONTH_DAY = "MM月dd日";
    public static final String DATE_FORMAT_HOUR_MINUTE = "HH:mm";

    /**
     * @param date1 时间A
     * @param date2 时间B
     * @return 是否2个为同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return !(date1 == null || date2 == null) && diffDay(date1, date2) == 0;
    }

    /**
     * 计算两个日期之间相差的天数.不包括今天。
     *
     * @param date1 时间A
     * @param date2 时间B
     * @return 时间A与时间B的差值(前者 - 后者)。当有任何一个时间为空时，返回null
     * @see #diffDay(Date, Date, boolean) 取代。
     * 原因：该方法不精准。
     */
    public static Integer diffDay(Date date1, Date date2) {
        return diffDay(date1, date2, false);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param fDate        时间A
     * @param oDate        时间B
     * @param includeToday 是否包括今天
     * @return 时间A与时间B的差值(前者 - 后者)。当有任何一个时间为空时，返回null
     */
    public static Integer diffDay(Date fDate, Date oDate, boolean includeToday) {
        if (fDate == null || oDate == null) {
            return null;
        }
        DateTime begin = new DateTime(fDate);
        DateTime end = new DateTime(oDate);
        int days = Days.daysBetween(end.toLocalDate(), begin.toLocalDate()).getDays();
        return includeToday ? days + 1 : days;
    }

    /**
     * 计算量个日期之间相差的小时数
     *
     * @param fDate       时间A
     * @param oDate       时间B
     * @param includeHour 是否包括当前小时
     * @return 时间A与时间B的差值(前者 - 后者)。当有任何一个时间为空时，返回null
     */
    @SuppressWarnings("UnusedDeclaration")
    public static Integer diffHour(Date fDate, Date oDate, boolean includeHour) {
        if (fDate == null || oDate == null) {
            return null;
        }
        DateTime begin = new DateTime(fDate);
        DateTime end = new DateTime(oDate);
        int hours = Hours.hoursBetween(end.toLocalDate(), begin.toLocalDate()).getHours();
        return includeHour ? hours + 1 : hours;
    }

    /**
     * @param date 时间
     * @return 参数日期是否为今天
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    /**
     * 计算当期时间相差的日期
     *
     * @param field  日历字段.<br/>eg:Calendar.MONTH,Calendar.DAY_OF_MONTH,<br/>Calendar.HOUR_OF_DAY等.
     * @param amount 相差的数值
     * @return 计算后的日志
     */
    public static Date addDate(int field, int amount) {
        return addDate(null, field, amount);
    }

    /**
     * 计算当期时间相差的日期
     *
     * @param date   设置时间
     * @param field  日历字段.<br/>eg:Calendar.MONTH,Calendar.DAY_OF_MONTH,<br/>Calendar.HOUR_OF_DAY等.
     * @param amount 相差的数值
     * @return 计算后的日志
     */
    public static Date addDate(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        c.add(field, amount);
        return c.getTime();
    }

    /**
     * 设置指定时间的相关field
     *
     * @param date   设置时间
     * @param field  日历字段.<br/>eg:Calendar.MONTH,Calendar.DAY_OF_MONTH,<br/>Calendar.HOUR_OF_DAY等.
     * @param amount 设置的值
     * @return 计算后的日志
     */
    public static Date setDate(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        c.set(field, amount);
        return c.getTime();
    }

    /**
     * 设置指定时间的相关field
     *
     * @param date        时间
     * @param hour        小时，当null的时候，不进行改变
     * @param minute      分钟，当null的时候，不进行改变
     * @param second      秒，当null的时候，不进行改变
     * @param milliSecond 毫秒，当null的时候，不进行改变
     * @return 结果
     */
    public static Date setDate(Date date, Integer hour, Integer minute, Integer second, Integer milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (hour != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != null) {
            calendar.set(Calendar.MINUTE, minute);
        }
        if (second != null) {
            calendar.set(Calendar.SECOND, second);
        }
        if (milliSecond != null) {
            calendar.set(Calendar.MILLISECOND, milliSecond);
        }
        return calendar.getTime();
    }

    /**
     * @param date 日期
     * @return 获得该日期的开始
     */
    public static Date getDayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setCalender(calendar, 0, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * @param date 日期
     * @return 获得该日期的结束
     */
    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setCalender(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    public static Date getMinuteBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setCalender(calendar, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                0, 0);
        return calendar.getTime();
    }

    public static Date getMinuteEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setCalender(calendar, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                59, 999);
        return calendar.getTime();
    }

    /**
     * 获取该时间所在的小时开始时间点
     */
    public static Date getHourBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取该时间所在的小时结束时间点
     */
    public static Date getHourEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取该日期当月第一天
     *
     * @param date
     * @return
     */
    public static Date getMonthBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDayBegin(date));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取该日期当月最后一天
     *
     * @param date
     * @return
     */
    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDayEnd(date));
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 判断该日期为星期几？
     *
     * @param pTime   日期
     * @param pattern 时间格式化(若为空,则pattern 默认为 'yyyy/MM/dd HH:mm:ss')
     * @return 1-星期一,2-星期二,.....,6-星期六,7-星期天
     */
    public static Integer dayForWeek(String pTime, String pattern) throws Exception {
        if (StringUtil.isEmpty(pattern)) {
            pattern = DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
        }
        Date date = parse(pTime, pattern);
        return dayForWeek(date);
    }

    /**
     * 判断该日期为星期几？
     *
     * @param date 日期
     * @return 1-星期一,2-星期二,.....,6-星期六,7-星期天
     */
    public static Integer dayForWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer dayForWeek = 0;
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 取得日期所在周的下星期一<br />
     * 注：若参数date为空，则取得第当前日期所对应的下一周
     *
     * @param date 所在日期
     */
    public static Date getNextMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        // 没有设置日期,则取当前时间
        if (date != null) {
            cal.setTime(date);
        }
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day != Calendar.SUNDAY) {
            cal.add(Calendar.WEEK_OF_MONTH, 1);
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return cal.getTime();
    }

    /**
     * 取得日期所在周的下星期日<br />
     * 注：若参数date为空，则取得第当前日期所对应的下一周
     *
     * @param date 所在日期
     */
    public static Date getNextSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        // 没有设置日期,则取当前时间
        if (date != null) {
            cal.setTime(date);
        }
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (day != Calendar.SUNDAY) {
            cal.add(Calendar.WEEK_OF_MONTH, 2);
        } else {
            cal.add(Calendar.WEEK_OF_MONTH, 1);
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * 取得日期所在周的上个星期一<br />
     * 注：若参数date为空，则取得第当前日期所对应的上一周
     *
     * @param date
     * @return
     */
    public static Date getBeforeWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        //n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
        int n = -1;
        String monday;
        cal.add(Calendar.DATE, n * 7);

        //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 取得日期所在月的上个月同一天<br />
     * 注：若参数date为空，则取得第当前月所对应的上一月
     *
     * @param date
     * @return
     */
    public static Date getBeforeMonthDay(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * 取得日期所在周的周末<br />
     * 注：若参数date为空，则取得第当前日期所对应的下一周
     *
     * @param date
     * @return
     */
    public static Date getWeekEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        if (date != null) {
            cal.setTime(date);
        }
        //n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
        int n = 0;
        cal.add(Calendar.DATE, n * 7);

        //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * 获取本周开始时间
     *
     * @param date
     * @return
     */
    public static Date getWeekBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        if (date != null) {
            cal.setTime(date);
        }
        //n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
        int n = 0;
        cal.add(Calendar.DATE, n * 7);

        //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 取得日期所在的前一天
     *
     * @param date
     * @return
     */
    public static Date getDayBefore(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        return c.getTime();
    }

    /**
     * 取得所在日期的下一天<br/>
     * 注：如date为空,则取到为明天
     *
     * @param date 所在日期
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 设置Calendar的小时、分钟、秒、毫秒
     *
     * @param calendar    日历
     * @param hour        小时
     * @param minute      分钟
     * @param second      秒
     * @param milliSecond 毫秒
     */
    public static void setCalender(Calendar calendar, int hour, int minute, int second, int milliSecond) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, milliSecond);
    }

    /**
     * @param date    时间。若为空，则返回空串
     * @param pattern 时间格式化
     * @return 格式化后的时间字符串.
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化日期
     *
     * @param date    时间。若为空，则返回空串
     * @param pattern 时间格式化
     * @param locale  本地化
     * @return 格式化后的时间字符串
     */
    public static String format(Date date, String pattern, Locale locale) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatTimestamp(Long time, String pattern) {
        return DateUtil.format(new Date(time), pattern);
    }

    /**
     * @param date 时间。不允许空
     * @return 时间对应的int值。比如说2013/3/4转换为20130304
     */
    public static Integer formatDate2Int(Date date) {
        Assert.notNull(date, "date is null!");
        return Integer.valueOf(format(date, "yyyyMMdd"));
    }

    /**
     * @param year        年
     * @param mouth       月
     * @param day         日
     * @param hour        小时
     * @param minute      分钟
     * @param second      秒
     * @param milliSecond 毫秒
     * @return 指定时间
     */
    @SuppressWarnings("UnusedDeclaration")
    public static Date buildTime(Integer year, Integer mouth, Integer day, Integer hour, Integer minute,
                                 Integer second, Integer milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, mouth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, milliSecond);
        return calendar.getTime();
    }

    /**
     * 判断当时时间是否在某个时间范围内
     *
     * @param minHour
     * @param minMin
     * @param maxHour
     * @param maxMin
     * @return
     */
    public static boolean isBetweenTheTimeZone(int minHour, int minMin, int maxHour, int maxMin) {
        return isBetweenTheTimeZone(minHour * 100 + minMin, maxHour * 100 + maxMin);
    }

    public static boolean isBetweenTheTimeZone(int beginTime, int endTime) {
        return isBetweenTheTimeZone(null, beginTime, endTime);
    }

    public static boolean isBetweenTheTimeZone(Date date, int beginTime, int endTime) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int now = calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE);
        return beginTime <= now && now <= endTime;
    }

    /**
     * 获得时间指定的field
     *
     * @param time  时间
     * @param field filed
     * @return 值
     */
    public static int getField(Date time, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(field);
    }

    /**
     * 格式化time
     *
     * @param time
     * @return
     */
    public static Date parse(Long time) {
        return new Date(time);
    }

    public static void main(String[] args) {
//        System.out.println(DateUtil.getDayBegin(DateUtil.addDate(Calendar.DAY_OF_YEAR, -1)).getTime());
//        Map<String, String> map = DateUtil.getCurrentTime();
//        for(String key : map.keySet()) {
//            System.out.println(key + " = "+ map.get(key));
//        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Map<String, String> getCurrentTime() {
        Map<String, String> map = new HashMap<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        int week = c.get(Calendar.DAY_OF_WEEK);
        int month = c.get(Calendar.MONTH);
        switch (week) {
            case 1:
                map.put("WEEK_EN", "Sunday");
                map.put("WEEK_CN", "星期天");
                break;
            case 2:
                map.put("WEEK_EN", "Monday");
                map.put("WEEK_CN", "星期一");
                break;
            case 3:
                map.put("WEEK_EN", "Tuesday");
                map.put("WEEK_CN", "星期二");
                break;
            case 4:
                map.put("WEEK_EN", "Wednesday");
                map.put("WEEK_CN", "星期三");
                break;
            case 5:
                map.put("WEEK_EN", "Thursday");
                map.put("WEEK_CN", "星期四");
                break;
            case 6:
                map.put("WEEK_EN", "Friday");
                map.put("WEEK_CN", "星期五");
                break;
            case 7:
                map.put("WEEK_EN", "Saturday");
                map.put("WEEK_CN", "星期六");
                break;
        }

        switch (month) {
            case 0 :
                map.put("MONTH", "Jan");
                break;
            case 1 :
                map.put("MONTH", "Feb");
                break;
            case 2 :
                map.put("MONTH", "Mar");
                break;
            case 3 :
                map.put("MONTH", "Apr");
                break;
            case 4 :
                map.put("MONTH", "May");
                break;
            case 5 :
                map.put("MONTH", "Jun");
                break;
            case 6 :
                map.put("MONTH", "Jul");
                break;
            case 7 :
                map.put("MONTH", "Aug");
                break;
            case 8 :
                map.put("MONTH", "Sep");
                break;
            case 9 :
                map.put("MONTH", "Oct");
                break;
            case 10 :
                map.put("MONTH", "Nov");
                break;
            case 11 :
                map.put("MONTH", "Dec");
                break;

        }
        map.put("DAY", c.get(Calendar.DAY_OF_MONTH) +"");
        map.put("TIME", DateUtil.format(new Date(), "yyyy-MM-dd"));
        return map;
    }
}