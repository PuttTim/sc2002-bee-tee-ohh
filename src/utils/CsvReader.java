package utils;

import interfaces.ICsvConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import interfaces.ICsvConfig;

/**
 * Utility class for reading CSV files into a list of key-value maps.
 */
public class CsvReader {
    private static final Pattern CSV_PATTERN = Pattern.compile(
        ",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))"  // Split on comma but not within quotes
    );

    /**
     * Reads a CSV file based on the given configuration.
     *
     * @param config the CSV configuration, including file path and headers
     * @return a list of maps where each map represents a row in the CSV,
     * with headers mapped to corresponding values
     * @throws IOException if the file cannot be read
     */
    public static List<Map<String, String>> read(ICsvConfig config) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        List<String> headers = config.getHeaders();

        try (BufferedReader br = new BufferedReader(new FileReader(config.getFilePath()))) {
            String line;
            // Skip header line since we're using configured headers
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] values = CSV_PATTERN.split(line);
                Map<String, String> record = new HashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    String value = i < values.length ? unescapeValue(values[i]) : "";
                    record.put(headers.get(i), value);
                }

                records.add(record);
            }
        }

        return records;
    }

    /**
     * Cleans up value reads from CSV lines:
     * <ul>
     *   <li>Trims whitespace</li>
     *   <li>Removes surrounding quotes</li>
     *   <li>Replaces double quotes with single quotes</li>
     * </ul>
     *
     * @param value the raw CSV value
     * @return a cleaned-up string
     */
    private static String unescapeValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        value = value.trim();
        // Remove surrounding quotes if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        // Replace escaped quotes with single quotes
        value = value.replace("\"\"", "\"");

        return value;
    }
}
