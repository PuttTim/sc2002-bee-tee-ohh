package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.ICsvConfig;

public class CsvReader {
    public static List<Map<String, String>> read(ICsvConfig config) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(config.getFilePath()))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return records;
            }

            String line;
            String[] headers = config.getHeaders();
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new HashMap<>();
                
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    record.put(headers[i], values[i]);
                }
                
                records.add(record);
            }
        }
        
        return records;
    }
}
