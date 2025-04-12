package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateTimeStr, formatter != null ? formatter : DEFAULT_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DEFAULT_FORMATTER);
    }
    
    public static String formatDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter != null ? formatter : DEFAULT_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DEFAULT_FORMATTER);
    }
}
