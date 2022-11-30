package com.csc.common.utils.date;

import com.csc.common.enums.DateFormatEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: csc
 * @create: 2022/11/24
 * @Version: 1.0
 */
public class LocalDateTimeUtils {

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
}
