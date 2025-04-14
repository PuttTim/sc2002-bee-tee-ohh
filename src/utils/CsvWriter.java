package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import interfaces.ICsvConfig;

public class CsvWriter {
    public static void write(ICsvConfig config, List<Map<String, String>> records) throws IOException {
        try (FileWriter writer = new FileWriter(config.getFilePath())) {
            // Write headers
            writer.write(String.join(",", config.getHeaders()) + "\n");
            
            // Write records
            for (Map<String, String> record : records) {
                String[] values = new String[config.getHeaders().size()];
                for (int i = 0; i < config.getHeaders().size(); i++) {
                    String value = record.get(config.getHeaders().get(i));
                    values[i] = value != null ? value : "";
                }
                writer.write(String.join(",", values) + "\n");
            }
        }
    }
}
