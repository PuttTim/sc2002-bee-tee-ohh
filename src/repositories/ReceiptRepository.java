package repositories;

import models.Receipt;
import models.enums.FlatType;
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
            return List.of("ReceiptID", "ApplicantNRIC", "ApplicantAge", "MaritalStatus",
                          "FlatType", "FlatPrice", "FlatUnitNumber", "ProjectID",
                          "ProjectName", "ProjectLocation", "ReceiptDate");
        }
    }

    private static List<Receipt> receipts = new ArrayList<>();

    private ReceiptRepository() {} // private constructor

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Receipt receipt : receipts) {
            Map<String, String> record = new HashMap<>();
            record.put("ReceiptID", receipt.getReceiptID());
            record.put("ApplicantNRIC", receipt.getApplicantNRIC());
            record.put("ApplicantAge", String.valueOf(UserRepository.getByNRIC(receipt.getApplicantNRIC()).getAge()));
            record.put("MaritalStatus", UserRepository.getByNRIC(receipt.getApplicantNRIC()).getMaritalStatus().toString());
            record.put("FlatType", receipt.getFlatType().toString());
            record.put("FlatPrice", String.valueOf(receipt.getFlatPrice()));
            record.put("FlatUnitNumber", receipt.getFlatUnitNumber());
            record.put("ProjectID", receipt.getProjectID());
            record.put("ProjectName", receipt.getProjectName());
            record.put("ProjectLocation", ProjectRepository.getById(receipt.getProjectID()).getLocation());
            record.put("ReceiptDate", DateTimeUtils.formatDateTime(receipt.getReceiptDate()));
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
                // Get the applicant and project objects
                models.Applicant applicant = ApplicantRepository.getByNRIC(record.get("ApplicantNRIC"));
                models.Project project = ProjectRepository.getById(record.get("ProjectID"));
                
                if (applicant == null || project == null) {
                    System.err.println("Could not find applicant or project for receipt: " + record.get("ReceiptID"));
                    continue;
                }

                Receipt receipt = new Receipt(
                    applicant,
                    FlatType.valueOf(record.get("FlatType")),
                    Integer.parseInt(record.get("FlatPrice")),
                    record.get("FlatUnitNumber"),
                    project
                );
                receipts.add(receipt);
            }
            System.out.println("Loaded " + receipts.size() + " receipts from CSV.");
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
            .filter(receipt -> receipt.getReceiptID().equals(applicationId))
            .findFirst()
            .orElse(null);
    }

    public static List<Receipt> getByApplicant(String applicantNRIC) {
        return receipts.stream()
            .filter(receipt -> receipt.getApplicantNRIC().equals(applicantNRIC))
            .toList();
    }
}