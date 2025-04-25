package utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class DateTimeUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter DD_MM_YYYY_T_HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");
    
    public static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null; 
        }
        try {
            return LocalDateTime.parse(dateTimeStr.trim(), formatter != null ? formatter : DEFAULT_FORMATTER);
        } catch (Exception e) {
            System.err.println("Error parsing date: '" + dateTimeStr + "' - " + e.getMessage());
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

    public static LocalDateTime getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now(
            java.time.ZoneId.of("Asia/Singapore")

        );
        return now;
    }
}
