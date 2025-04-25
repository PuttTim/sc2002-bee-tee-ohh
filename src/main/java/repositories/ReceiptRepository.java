package repositories;

import interfaces.ICsvConfig;
import models.Officer;
import models.Project;
import models.Receipt;
import models.User;
import models.enums.FlatType;
import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceiptRepository {
    private static class ReceiptCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/receipt.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("ReceiptID", "ApplicantNRIC", "FlatType", "FlatPrice", "FlatUnitNumber", "ProjectID", "BookingTimestamp", "OfficerNRIC"
            );
        }
    }

    private static List<Receipt> receipts = new ArrayList<>();

    private ReceiptRepository() {}

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Receipt receipt : receipts) {
            Map<String, String> record = new HashMap<>();
            record.put("ReceiptID", receipt.getReceiptId());
            record.put("ApplicantNRIC", receipt.getApplicantNRIC());
            record.put("FlatType", receipt.getFlatType().name());
            record.put("FlatPrice", String.valueOf(receipt.getFlatPrice()));
            record.put("FlatUnitNumber", receipt.getUnitNumber());
            record.put("ProjectID", receipt.getProjectID());
            record.put("BookingTimestamp", DateTimeUtils.formatDateTime(receipt.getBookingTimestamp()));
            record.put("OfficerNRIC", receipt.getOfficer().getUserNRIC());
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
                try {
                    User user = UserRepository.getByNRIC(record.get("ApplicantNRIC"));
                    Officer officer = OfficerRepository.getByNRIC(record.get("OfficerNRIC"));
                    Project project = ProjectRepository.getById(record.get("ProjectID"));

                    Receipt receipt = new Receipt(
                        record.get("ReceiptID"),
                        user.getName(),
                        user.getUserNRIC(),
                        user.getAge(),
                        user.getMaritalStatus(),
                        FlatType.valueOf(record.get("FlatType")),
                        Integer.parseInt(record.get("FlatPrice")),
                        record.get("FlatUnitNumber"),
                        project.getProjectName(),
                        record.get("ProjectID"),
                        project.getLocation(),
                        DateTimeUtils.parseDateTime(record.get("BookingTimestamp")),
                        officer
                        );
                    receipts.add(receipt);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing age for receipt " + record.get("ReceiptID") + ": " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing enum/date/time for receipt " + record.get("ReceiptID") + ": " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error creating receipt object for ID " + record.get("ReceiptID") + ": " + e.getMessage());
                }
            }
            System.out.println("Loaded " + receipts.size() + " receipts from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading receipts: " + e.getMessage());
            receipts = new ArrayList<>();
        }
    }

    public static List<Receipt> getAll() {
        return new ArrayList<>(receipts);
    }

    public static void add(Receipt receipt) {
        if (getById(receipt.getReceiptId()) == null) {
            receipts.add(receipt);
            saveAll();
        } else {
            System.err.println("Receipt with ID " + receipt.getReceiptId() + " already exists. Cannot add duplicate.");
        }
    }

    public static Receipt getById(String receiptId) {
        return receipts.stream()
            .filter(receipt -> receipt.getReceiptId().equals(receiptId))
            .findFirst()
            .orElse(null);
    }

    public static List<Receipt> getByApplicantNRIC(String applicantNRIC) {
        return receipts.stream()
            .filter(receipt -> receipt.getApplicantNRIC().equals(applicantNRIC))
            .collect(Collectors.toList());
    }

    public static List<Receipt> getByProjectID(String projectID) {
        return receipts.stream()
            .filter(receipt -> receipt.getProjectID().equals(projectID))
            .collect(Collectors.toList());
    }
}