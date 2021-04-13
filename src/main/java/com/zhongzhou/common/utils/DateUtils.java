package com.zhongzhou.common.utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtils {

    /**
     * 获取今天
     *
     * @return String
     */
    public static String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 获取昨天
     *
     * @return String
     */
    public static String getYestoday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取本周的第一天
     *
     * @return String
     **/
    public static String getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取本周的第一天
     *
     * @return String
     **/
    public static Date getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        return cal.getTime();
    }

    /**
     * 获取本周的最后一天
     *
     * @return String
     **/
    public static String getWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取本周的最后一天
     *
     * @return String
     **/
    public static Date getWeekEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    /**
     * 获取本月开始日期
     *
     * @return String
     **/
    public static String getMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取本月最后一天
     *
     * @return String
     **/
    public static String getMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取本年的第一天
     *
     * @return String
     **/
    public static String getYearStart() {
        return new SimpleDateFormat("yyyy").format(new Date()) + "-01-01";
    }

    /**
     * 获取本年的最后一天
     *
     * @return String
     **/
    public static String getYearEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currYearLast = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currYearLast);
    }

    /**
     * 得到当前年份 yyyy
     *
     * @return String
     */
    public static String getCurrentYear() {
        //日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        //得到当前年份yyyy
        return dateFormat.format(new Date());
    }

    /**
     * 得到当前月份 MM
     *
     * @return String
     */
    public static String getCurrentMonth() {
        //日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        //得到当前年份yyyy
        return dateFormat.format(new Date());
    }

    /**
     * 得到本年的所有月份 yyyy-MM
     *
     * @return List
     */
    public static List<String> getCurrentYearByMonthList() {
        //result
        List<String> result = new ArrayList<>();
        //得到当前年份yyyy
        String year = getCurrentYear();
        //循环1~12月
        for (int i = 1; i <= 12; i++) {
            if (i <= 9) {
                result.add(year + "-0" + i);
            } else {
                result.add(year + "-" + i);
            }
        }
        //return data
        return result;
    }

    /**
     * 得到本月的天数 yyyy-MM-dd
     *
     * @return List
     */
    public static List<String> getCurrentMonthByDay() {
        //result
        List<String> result = new ArrayList<>();
        //得到当前年份yyyy
        String year = getCurrentYear();
        //得到当前月份 MM
        String month = getCurrentMonth();
        //本月最大天数
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        //本月所有天
        for (int i = 1; i <= day; i++) {
            if (i <= 9) {
                result.add(year + "-" + month + "-0" + i);
            } else {
                result.add(year + "-" + month + "-" + i);
            }
        }
        return result;
    }

    /**
     * 得到本周的天数 yyyy-MM-dd
     *
     * @return List
     */
    public static List<String> getCurrentWeekDay() {
        //本周第一天
        Date beginDate = getWeekStartDate();
        //本周最后一天
        Date endDate = getWeekEndDate();
        List<Date> dateList = new ArrayList();
        dateList.add(beginDate);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);
        // 测试此日期是否在指定日期之后
        while (endDate.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calBegin.getTime());
        }
        //result
        List<String> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Date date : dateList) {
            result.add(dateFormat.format(date));
        }
        //return result
        return result;
    }

    public static void main(String[] args) {
        Long value = Long.valueOf("1336951365337624577");
        System.out.println(value);
    }
    public static void  soutDateList(List<String> dateList){
        dateList.forEach(System.out::println);
    }

}