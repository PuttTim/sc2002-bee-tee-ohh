package utils;

import interfaces.ICsvConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utility class for writing data to CSV files.
 * <p>
 * Values are written according to headers defined in the ICsvConfig.
 * Escapes values as needed to preserve CSV formatting.
 * </p>
 */
public class CsvWriter {

    /**
     * Writes a list of records to a CSV file using the configuration provided.
     *
     * @param config  the CSV configuration including file path and headers
     * @param records the list of records to write, where each record is a map of header to value
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

    private static void writeLine(BufferedWriter writer, List<String> values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                writer.write(',');
            }
            writer.write(escapeValue(values.get(i)));
        }
        writer.write('\n');
    }

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

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
