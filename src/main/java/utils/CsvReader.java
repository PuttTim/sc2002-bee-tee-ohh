package utils;

import interfaces.ICsvConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class for reading CSV files into a list of key-value maps.
 * <p>
 * Uses headers defined in the given ICsvConfig to map values from each line.
 * Handles values enclosed in quotes and escaped commas within quoted values.
 * </p>
 */
public class CsvReader {
    private static final Pattern CSV_PATTERN = Pattern.compile(
            ",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))"  // Split on commas outside quotes
    );

    /**
     * Reads a CSV file and returns a list of records represented as maps from header to value.
     *
     * @param config the CSV configuration containing file path and headers
     * @return a list of records, each as a map of column name to value
     * @throws IOException if the file cannot be read
     */
    public static List<Map<String, String>> read(ICsvConfig config) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        List<String> headers = config.getHeaders();

        try (BufferedReader br = new BufferedReader(new FileReader(config.getFilePath()))) {
            br.readLine(); // Skip header line

            String line;
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

    private static String unescapeValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        value = value.trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        return value.replace("\"\"", "\"");
    }
}
