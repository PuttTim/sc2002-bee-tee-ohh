package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr.trim(), formatter != null ? formatter : DEFAULT_FORMATTER);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateTimeStr + " - " + e.getMessage());
            return null;
        }
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DEFAULT_FORMATTER);
    }
    
    public static String formatDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) {
            return "";
        }
        try {
            return dateTime.format(formatter != null ? formatter : DEFAULT_FORMATTER);
        } catch (Exception e) {
            System.err.println("Error formatting date: " + e.getMessage());
            return "";
        }
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DEFAULT_FORMATTER);
    }
}
