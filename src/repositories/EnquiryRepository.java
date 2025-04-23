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
 * A repository class for managing enquiries related to projects.
 * Supports loading and saving data to CSV and searching enquiries by different fields.
 */
public class EnquiryRepository {

    /**
     * Configuration for reading and writing the CSV file containing enquiries.
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

    private static List<Enquiry> enquiries = new ArrayList<>();

    private EnquiryRepository() {}

    /**
     * Saves all enquiries to the CSV file.
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
     * Loads all enquiries from the CSV file.
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
     * Returns all enquiries in the system.
     *
     * @return list of all enquiries
     */
    public static List<Enquiry> getAll() {
        return enquiries;
    }

    /**
     * Returns a list of enquiries related to a specific project.
     *
     * @param projectId the ID of the project
     * @return list of enquiries related to the given project
     */
    public static List<Enquiry> getEnquiriesByProject(String projectId) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getProjectID().equals(projectId))
                .collect(Collectors.toList());
    }

    /**
     * Adds a new enquiry to the repository and saves it to the CSV.
     *
     * @param enquiry the enquiry to add
     */
    public static void add(Enquiry enquiry) {
        enquiries.add(enquiry);
        saveAll();
    }

    /**
     * Finds an enquiry by its ID.
     *
     * @param enquiryId the ID of the enquiry to search for
     * @return the matching enquiry, or <code>null</code> if not found
     */
    public static Enquiry getEnquiryById(String enquiryId) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getEnquiryID().equals(enquiryId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Deletes an enquiry from the repository and saves the changes.
     *
     * @param enquiry the enquiry to delete
     */
    public static void delete(Enquiry enquiry) {
        enquiries.remove(enquiry);
        saveAll();
    }
}
