package utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Utility class for parsing and formatting {@link LocalDateTime} objects.
 * <p>
 * Uses ISO_LOCAL_DATE_TIME format by default. Supports null-safe operations.
 * </p>
 */
public class DateTimeUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * A DateTimeFormatter for formatting and parsing date-time strings in this
     * pattern: date (day, month, year) followed by a 'T' separator,
     * then the time (hour, minute, second).
     *
     * Example: "25-04-2025T14:30:00"
     */
    public static final DateTimeFormatter DD_MM_YYYY_T_HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");

    /**
     * Parses a date-time string into a {@link LocalDateTime} using the specified formatter.
     *
     * @param dateTimeStr the string to parse
     * @param formatter the formatter to use, or null to use default
     * @return the parsed LocalDateTime, or null if input is invalid
     */
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

    /**
     * Parses a date-time string using the default formatter.
     *
     * @param dateTimeStr the string to parse
     * @return the parsed LocalDateTime, or null if input is invalid
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DEFAULT_FORMATTER);
    }

    /**
     * Formats a {@link LocalDateTime} as a string using the specified formatter.
     *
     * @param dateTime the date-time to format
     * @param formatter the formatter to use, or null to use default
     * @return the formatted string, or empty string if dateTime is null
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
     * Formats a {@link LocalDateTime} using the default formatter.
     *
     * @param dateTime the date-time to format
     * @return the formatted string, or empty string if dateTime is null
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DEFAULT_FORMATTER);
    }

    /**
     * Returns the current date and time in the Asia/Singapore time zone.
     *
     * @return the current LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(java.time.ZoneId.of("Asia/Singapore"));
    }
}
