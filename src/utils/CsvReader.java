package utils;

import interfaces.ICsvConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class CsvReader {
    private static final Pattern CSV_PATTERN = Pattern.compile(
        ",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))"  // Split on comma but not within quotes
    );

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
