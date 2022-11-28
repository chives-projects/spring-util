package com.csc.spring.common.utils.date;

import com.csc.spring.common.enums.DateFormatEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: csc
 * @create: 2022/11/24
 * @Version: 1.0
 */
public class LocalDateTimeUtils {

    public static LocalTime startTime = LocalTime.of(0, 0, 0);
    public static LocalTime endTime = LocalTime.of(23, 59, 59);

    public static LocalDateTime toLocalDateTime(String dateTime) {
        return toLocalDateTime(dateTime, DateFormatEnum.YYYY_MM_DD_HH_MM_SS);
    }

    public static LocalDateTime intToLocalDateTime(String dateTime) {
        return toLocalDateTime(dateTime, DateFormatEnum.YYYYMMDDHHMMSS);
    }

    public static LocalDateTime toLocalDateTime(String dateTime, DateFormatEnum dateFormatEnum) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(dateFormatEnum.getFormat()));
    }

    public static String toIntDate(LocalDateTime localDateTime) {
        return toString(localDateTime, DateFormatEnum.YYYYMMDD);
    }

    public static String toStringDateTime(LocalDateTime localDateTime) {
        return toString(localDateTime, DateFormatEnum.YYYY_MM_DD_HH_MM_SS);
    }

    public static String toIntDateTime(LocalDateTime localDateTime) {
        return toString(localDateTime, DateFormatEnum.YYYYMMDDHHMMSS);
    }

    public static String toString(LocalDateTime localDateTime, DateFormatEnum dateFormatEnum) {
        return localDateTime.format(DateTimeFormatter.ofPattern(dateFormatEnum.getFormat()));
    }

    public static LocalDateTime getMin(LocalDateTime... date) {
        LocalDateTime res = LocalDateTime.MAX;
        for (LocalDateTime localDate : date) {
            if (res.isAfter(localDate)) res = localDate;
        }
        return res;
    }

    public static LocalDateTime toStartLocalDateTime(LocalDate date) {
        return LocalDateTime.of(date, startTime);
    }

    public static LocalDateTime toEndLocalDateTime(LocalDate date) {
        return LocalDateTime.of(date, endTime);
    }
}
