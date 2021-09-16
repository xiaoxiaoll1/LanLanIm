package com.zj.util;

import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaozj
 */
public class DateUtils {

    /**
     * 默认使用系统当前时区
     */
    private static final ZoneId ZONE = ZoneId.systemDefault();

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_FORMAT_DS = "yyyyMMdd";

    private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    private static final String TIME_FORMAT = "yyyyMMddHHmmss";

    private static final String REGEX = "\\:|\\-|\\s";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

        LocalDateTime now = LocalDateTime.now();

        return now.format(dateTimeFormatter);

    }

    /**
     * 获取昨日时间
     *
     * @param format
     * @return
     */
    public static String getYesterday(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate yesterday = nowDate.minusDays(1);
        return yesterday.format(dateTimeFormatter);

    }

    /**
     * 获取上周的时间
     *
     * @param format
     * @return
     */
    public static String getLastWeek(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastWeek = nowDate.minusWeeks(1);
        return lastWeek.format(dateTimeFormatter);
    }

    /**
     * 获取上个月的时间
     *
     * @param format
     * @return
     */
    public static String getLastMonth(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastMonth = nowDate.minusMonths(1);
        return lastMonth.format(dateTimeFormatter);

    }

    /**
     * 获取去年的时间
     *
     * @param format
     * @return
     */
    public static String getLastYear(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastYear = nowDate.minusYears(1);
        return lastYear.format(dateTimeFormatter);
    }

    /**
     * 获取前多少天的日期
     *
     * @param format
     * @param num
     * @return
     */
    public static String getBeforeSomeDay(String format, int num) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate beforeDay = nowDate.minusDays(num);
        return beforeDay.format(dateTimeFormatter);
    }

    /**
     * 获取指定时间的前多少天
     *
     * @param format
     * @param date
     * @param num
     * @return
     */
    public static String getBeforeDayOfDate(String format, String date, int num) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDate beforeDay = localDate.minusDays(num);
        return beforeDay.format(dateTimeFormatter);

    }

    /**
     * 获取当天的开始时间  yyyy-MM-dd 00:00:00
     *
     * @param format
     * @return
     */
    public static String getDayStartTime(String format, String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime toDayStart = LocalDateTime.of(localDate, LocalTime.MIN);
        return toDayStart.format(FORMATTER);
    }

    /**
     * 获取当天的结束时间 yyyy-MM-dd 23:59:59
     *
     * @param format
     * @return
     */
    public static String getDayEndTime(String format, String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime toDayStart = LocalDateTime.of(localDate, LocalTime.MAX);
        return toDayStart.format(FORMATTER);
    }

    /**
     * 获取两个时间之间的间隔天数
     *
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return
     */
    public static long getRangeCountOfDate(String startDate, String endDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS);
        LocalDate startLocalDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, dateTimeFormatter);
        long count = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        return count;

    }

    /**
     * 后期两个时间之间的所有日期 【包含开始时间和结束时间】
     *
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return
     */
    public static List<String> getRangeOfDate(String startDate, String endDate) {
        List<String> range = Lists.newArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS);
        LocalDate startLocalDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, dateTimeFormatter);
        long count = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        if (count < 0) {
            return range;
        }

        range = Stream.iterate(startLocalDate, d -> d.plusDays(1)).limit(count + 1).map(
                s -> s.format(dateTimeFormatter)).collect(Collectors.toList());
        return range;
    }

    public static void main(String[] args) {
        System.out.println(getCurrentTime("yyyy-MM-dd"));
    }

}



