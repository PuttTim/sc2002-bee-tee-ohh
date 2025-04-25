package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Enquiry;
import models.enums.EnquiryStatus;

import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

/**
 * Repository class responsible for managing enquiry data.
 * <p>
 * This class provides methods for loading, saving, and accessing enquiry data from and to CSV files.
 * It allows querying enquiries based on project, ID, and other details, and supports adding, updating, and deleting enquiries.
 * </p>
 */
public class EnquiryRepository {

    /**
     * Config class for reading and writing enquiry data in CSV format.
     */
    private static class EnquiryCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/enquiry.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("EnquiryID", "ProjectID", "ApplicantNRIC", "Query",
                    "Response", "EnquiryStatus", "EnquiryDate", "LastUpdated", "RespondedBy");
        }
    }

    // List to store enquiries
    private static List<Enquiry> enquiries = new ArrayList<>();

    // Private constructor to prevent instantiation
    private EnquiryRepository() {}

    /**
     * Saves all enquiries to the CSV file.
     * <p>
     * Each enquiry is written in CSV format, including details such as enquiry ID, project ID,
     * applicant NRIC, query, response, and status.
     * </p>
     */
    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Enquiry enquiry : enquiries) {
            Map<String, String> record = new HashMap<>();
            record.put("EnquiryID", enquiry.getEnquiryID());
            record.put("ProjectID", enquiry.getProjectID());
            record.put("ApplicantNRIC", enquiry.getApplicantNRIC());
            record.put("Query", enquiry.getQuery());
            record.put("Response", enquiry.getResponse() != null ? enquiry.getResponse() : "");
            record.put("EnquiryStatus", enquiry.getEnquiryStatus().toString());
            record.put("EnquiryDate", DateTimeUtils.formatDateTime(enquiry.getEnquiryDate()));
            record.put("LastUpdated", DateTimeUtils.formatDateTime(enquiry.getLastUpdated()));
            record.put("RespondedBy", enquiry.getRespondedBy() != null ? enquiry.getRespondedBy() : "");
            records.add(record);
        }

        try {
            CsvWriter.write(new EnquiryCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving enquiries: " + e.getMessage());
        }
    }

    /**
     * Loads enquiries from the CSV file and populates the enquiries list.
     * <p>
     * Each record in the CSV is used to create an enquiry object, including parsing the response, status, and other fields.
     * </p>
     */
    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new EnquiryCsvConfig());
            enquiries = new ArrayList<>();

            for (Map<String, String> record : records) {
                String response = record.get("Response");
                if (response != null && response.trim().isEmpty()) {
                    response = null;
                }

                String respondedBy = record.get("RespondedBy");
                if (respondedBy != null && respondedBy.trim().isEmpty()) {
                    respondedBy = null;
                }

                Enquiry enquiry = new Enquiry(
                        record.get("EnquiryID"),
                        record.get("ApplicantNRIC"),
                        record.get("ProjectID"),
                        record.get("Query"),
                        response,
                        EnquiryStatus.valueOf(record.get("EnquiryStatus")),
                        DateTimeUtils.parseDateTime(record.get("EnquiryDate")),
                        DateTimeUtils.parseDateTime(record.get("LastUpdated")),
                        respondedBy
                );
                enquiries.add(enquiry);
            }
            System.out.println("Loaded " + enquiries.size() + " enquiries from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading enquiries: " + e.getMessage());
        }
    }

    /**
     * Retrieves all enquiries.
     *
     * @return a list of all enquiries
     */
    public static List<Enquiry> getAll() {
        return enquiries;
    }

    /**
     * Retrieves a list of enquiries for a specific project.
     *
     * @param projectId the project ID
     * @return a list of enquiries for the specified project
     */
    public static List<Enquiry> getEnquiriesByProject(String projectId) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getProjectID().equals(projectId))
                .collect(Collectors.toList());
    }

    /**
     * Adds a new enquiry to the repository.
     *
     * @param enquiry the enquiry to be added
     */
    public static void add(Enquiry enquiry) {
        enquiries.add(enquiry);
        saveAll();
    }

    /**
     * Retrieves an enquiry by its ID.
     *
     * @param enquiryId the ID of the enquiry
     * @return the enquiry with the specified ID, or {@code null} if not found
     */
    public static Enquiry getEnquiryById(String enquiryId) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getEnquiryID().equals(enquiryId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Deletes a specific enquiry from the repository.
     *
     * @param enquiry the enquiry to be deleted
     */
    public static void delete(Enquiry enquiry) {
        enquiries.remove(enquiry);
        saveAll();
    }

    /**
     * Updates an existing enquiry in the repository.
     *
     * @param enquiry the enquiry to be updated
     */
    public static void update(Enquiry enquiry) {
        for (int i = 0; i < enquiries.size(); i++) {
            if (enquiries.get(i).getEnquiryID().equals(enquiry.getEnquiryID())) {
                enquiries.set(i, enquiry);
                break;
            }
        }
        saveAll();
    }
}
