package utils;

import interfaces.ICsvConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvWriter {
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

        // Escape quotes by doubling them and wrap in quotes
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
