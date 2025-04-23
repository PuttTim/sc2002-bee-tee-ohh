package utils;

import interfaces.ICsvConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utility class for writing data to CSV files.
 */
public class CsvWriter {
    /**
     * Writes the given records to a CSV file based on the given configuration.
     *
     * @param config  the CSV configuration including file path and headers
     * @param records the list of records to write, where each record maps header to value
     * @throws IOException if the file cannot be written
     */
    public static void write(ICsvConfig config, List<Map<String, String>> records) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getFilePath()))) {
            List<String> headers = config.getHeaders();
            
            // Write headers
            writeLine(writer, headers);
            
            // Write records
            for (Map<String, String> record : records) {
                List<String> values = headers.stream()
                    .map(header -> record.getOrDefault(header, ""))
                    .toList();
                writeLine(writer, values);
            }
        }
    }

    /**
     * Writes a line of CSV values to the writer.
     *
     * @param writer the BufferedWriter to write to
     * @param values the list of values to write in one row
     * @throws IOException if writing fails
     */
    private static void writeLine(BufferedWriter writer, List<String> values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                writer.write(',');
            }
            writer.write(escapeValue(values.get(i)));
        }
        writer.write('\n');
    }

    /**
     * Escapes a single value for safe CSV writing:
     * <ul>
     *   <li>Wraps the value in quotes if it contains commas, quotes, or newlines</li>
     *   <li>Escapes double quotes by doubling them</li>
     * </ul>
     *
     * @param value the original value
     * @return the escaped value
     */
    private static String escapeValue(String value) {
        if (value == null) {
            return "";
        }

        boolean needsQuoting = value.contains("\"") || 
                             value.contains(",") || 
                             value.contains("\n") || 
                             value.contains("\r") ||
                             value.trim().length() != value.length();

        if (!needsQuoting) {
            return value;
        }

        // Escape quotes by doubling them and wrap in quotes
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
