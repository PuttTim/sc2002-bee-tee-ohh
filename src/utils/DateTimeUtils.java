package utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Utility class for parsing and formatting date-time values.
 */
public class DateTimeUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Parses a string into a LocalDateTime using the given formatter.
     *
     * @param dateTimeStr the date-time string to parse
     * @param formatter the formatter to use; if null, uses the default ISO formatter
     * @return the parsed LocalDateTime, or <code>null</code> if parsing fails
     */
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

    /**
     * Parses a string into a LocalDateTime using the default ISO formatter.
     *
     * @param dateTimeStr the date-time string to parse
     * @return the parsed LocalDateTime, or <code>null</code> if parsing fails
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DEFAULT_FORMATTER);
    }

    /**
     * Formats a LocalDateTime into a string using the given formatter.
     *
     * @param dateTime the LocalDateTime to format
     * @param formatter the formatter to use; if null, uses the default ISO formatter
     * @return the formatted date-time string, or an empty string if formatting fails
     */
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

    /**
     * Formats a LocalDateTime into a string using the default ISO formatter.
     *
     * @param dateTime the LocalDateTime to format
     * @return the formatted date-time string, or an empty string if formatting fails
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DEFAULT_FORMATTER);
    }

    /**
     * Gets the current date and time in the Asia/Singapore time zone.
     *
     * @return the current LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now(
            java.time.ZoneId.of("Asia/Singapore")

        );
        return now;
    }
}