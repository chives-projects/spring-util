package com.eagle.common.utils.date;

import com.eagle.common.enums.DateFormatEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: csc
 * @create: 2022/11/24
 * @Version: 1.0
 */
public class LocalDateUtils {
    public static String toStandString(LocalDate localDate) {
        return toString(localDate, DateFormatEnum.YYYY_MM_DD);
    }

    public static String toInt(LocalDate localDate) {
        return toString(localDate, DateFormatEnum.YYYYMMDD);
    }

    public static LocalDate intTo(String localDate) {
        return LocalDate.parse(localDate, DateTimeFormatter.ofPattern(DateFormatEnum.YYYYMMDD.getFormat()));
    }

    public static String toString(LocalDate localDate, DateFormatEnum dateFormatEnum) {
        return localDate.format(DateTimeFormatter.ofPattern(dateFormatEnum.getFormat()));
    }
}
