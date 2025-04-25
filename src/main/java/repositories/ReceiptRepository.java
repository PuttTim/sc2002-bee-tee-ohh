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

/**
 * Repository for managing receipts, including loading from and saving to CSV files.
 * <p>
 * This class provides functionality to load receipts from a CSV file, save them back to the file,
 * and perform CRUD operations such as retrieving receipts by their ID or applicant NRIC.
 * </p>
 */
public class ReceiptRepository {

    /**
     * Configuration for reading and writing the receipt CSV file.
     */
    private static class ReceiptCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/receipt.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("ReceiptID", "ApplicantNRIC", "FlatType", "FlatPrice", "FlatUnitNumber", "ProjectID", "BookingTimestamp", "OfficerNRIC");
        }
    }

    private static List<Receipt> receipts = new ArrayList<>();

    private ReceiptRepository() {}

    /**
     * Saves all receipts to the CSV file.
     * <p>
     * The method iterates over all receipts, converts them to a map representation, and writes them
     * to the CSV file using the configured CSV writer.
     * </p>
     */
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

    /**
     * Loads receipts from the CSV file.
     * <p>
     * This method reads the CSV file, parses each record, and converts it into a `Receipt` object.
     * If any errors occur during parsing, they are logged.
     * </p>
     */
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

    /**
     * Retrieves all receipts.
     *
     * @return a list of all receipts
     */
    public static List<Receipt> getAll() {
        return new ArrayList<>(receipts);
    }

    /**
     * Adds a new receipt to the repository.
     * <p>
     * The method first checks if the receipt already exists in the repository using its ID. If it does not
     * exist, the receipt is added to the list and saved to the CSV file.
     * </p>
     *
     * @param receipt the receipt to add
     */
    public static void add(Receipt receipt) {
        if (getById(receipt.getReceiptId()) == null) {
            receipts.add(receipt);
            saveAll();
        } else {
            System.err.println("Receipt with ID " + receipt.getReceiptId() + " already exists. Cannot add duplicate.");
        }
    }

    /**
     * Retrieves a receipt by its ID.
     *
     * @param receiptId the ID of the receipt
     * @return the receipt with the specified ID, or null if no such receipt exists
     */
    public static Receipt getById(String receiptId) {
        return receipts.stream()
                .filter(receipt -> receipt.getReceiptId().equals(receiptId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all receipts associated with a specific applicant.
     *
     * @param applicantNRIC the NRIC of the applicant
     * @return a list of receipts for the specified applicant
     */
    public static List<Receipt> getByApplicantNRIC(String applicantNRIC) {
        return receipts.stream()
                .filter(receipt -> receipt.getApplicantNRIC().equals(applicantNRIC))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all receipts associated with a specific project.
     *
     * @param projectID the ID of the project
     * @return a list of receipts for the specified project
     */
    public static List<Receipt> getByProjectID(String projectID) {
        return receipts.stream()
                .filter(receipt -> receipt.getProjectID().equals(projectID))
                .collect(Collectors.toList());
    }
}
