package stores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class csvreading {
    public static void main(String[] args) {
        String csvFile = "src/stores/Projects.csv";
        String line;
        
        // predefined table headers
        String[] headers = {"Project ID", "Project Name", "Neighbourhood", "Flat Type", 
                          "Available Units", "Unit Price", "Start date", 
                          "End date", "Manager NRIC", "Officer Slots", 
                          "Officer 1", "Officer 2", "Officer 3"};
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // skip the first row of the csv file, use predefined headers
            br.readLine();
            
            // print the predefined table header
            printTableRow(headers, true);
            
            // read and print each line
            while ((line = br.readLine()) != null) {
                // split the data by commas
                String[] data = line.split(",");
                
                printTableRow(data, false);
            }
            
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }
    
    private static void printTableRow(String[] row, boolean isHeader) {
        // 13 columns
        int[] widths = {10, 15, 15, 10, 15, 12, 15, 15, 15, 15, 15, 15, 15};
        
        // print separators
        if (isHeader) {
            for (int width : widths) {
                System.out.print("+");
                printRepeatedChar('-', width);
            }
            System.out.println("+");
        }
        
        // print row data
        for (int i = 0; i < widths.length; i++) {
            System.out.print("|");
            // print empty string if data is empty (there should not be empty data)
            String cellContent = (i < row.length) ? row[i] : "";
            String formatted = String.format("%-" + widths[i] + "s", cellContent);
            System.out.print(formatted);
        }
        System.out.println("|");
        
        // print separator line
        for (int width : widths) {
            System.out.print("+");
            printRepeatedChar('-', width);
        }
        System.out.println("+");
    }
    
    private static void printRepeatedChar(char c, int times) {
        for (int i = 0; i < times; i++) {
            System.out.print(c);
        }
    }
}