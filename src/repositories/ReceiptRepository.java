package repositories;

import models.Project;
import models.Receipt;
import models.enums.FlatType;
import models.enums.MaritalStatus;
import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.ICsvConfig;

public class ReceiptRepository {
    private static class ReceiptCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/receipt.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("ApplicationID", "ApplicantName", "ApplicantNRIC", "ProjectName",
                          "FlatType", "Price", "MaritalStatus", "DateIssued");
        }
    }

    private static List<Receipt> receipts = new ArrayList<>();

    private ReceiptRepository() {} // private constructor

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Receipt receipt : receipts) {
            Map<String, String> record = new HashMap<>();
            record.put("ApplicationID", receipt.getReceiptID());
            record.put("ApplicantName", receipt.getApplicantName());
            record.put("ApplicantNRIC", receipt.getApplicantNRIC());
            record.put("ProjectName", receipt.getProjectName());
            record.put("FlatType", receipt.getFlatType().toString());
            record.put("Price", String.valueOf(receipt.getFlatPrice()));
            record.put("MaritalStatus", receipt.getMaritalStatus().toString());
            record.put("DateIssued", DateTimeUtils.formatDateTime(receipt.getReceiptDate()));
            records.add(record);
        }

        try {
            CsvWriter.write(new ReceiptCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving receipts: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ReceiptCsvConfig());
            receipts = new ArrayList<>();


            for (Map<String, String> record : records) {
                Receipt receipt = new Receipt(
                    ApplicantRepository.getByNRIC(record.get("ApplicantNRIC")),
                    FlatType.valueOf(record.get("Flat Type")),
                    Integer.parseInt(record.get("Price")),
                    record.get("Flat Unit Number"),
                    ProjectRepository.getByName(record.get("Project Name"))
                        
                );
                receipts.add(receipt);
            }
        } catch (IOException e) {
            System.err.println("Error loading receipts: " + e.getMessage());
        }
    }

    public static List<Receipt> getAll() {
        return receipts;
    }

    public static void add(Receipt receipt) {
        receipts.add(receipt);
    }

    public static Receipt getByApplicationId(String applicationId) {
        return receipts.stream()
            .filter(receipt -> receipt.getApplicationID().equals(applicationId))
            .findFirst()
            .orElse(null);
    }

    public static List<Receipt> getByApplicant(String applicantNRIC) {
        return receipts.stream()
            .filter(receipt -> receipt.getApplicantNRIC().equals(applicantNRIC))
            .toList();
    }
}