package repositories;

import models.Enquiry;
import models.enums.EnquiryStatus;
import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.ICsvConfig;

public class EnquiryRepository {
    private static class EnquiryCsvConfig implements ICsvConfig {
        @Override
        public String[] getHeaders() {
            return new String[] {
                "EnquiryID", "ApplicantNRIC", "ProjectID", "Query", 
                "Response", "Enquiry Status", "Enquiry Date", "Last Updated", "Responded By"
            };
        }

        @Override
        public String getFilePath() {
            return "data/enquiry.csv";
        }
    }

    private static List<Enquiry> enquiries = new ArrayList<>();

    private EnquiryRepository() {} // private constructor

    public static void saveAll() {
        try {
            List<Map<String, String>> records = new ArrayList<>();

            for (Enquiry enquiry : enquiries) {
                Map<String, String> record = new HashMap<>();
                record.put("EnquiryID", enquiry.getEnquiryID());
                record.put("ApplicantNRIC", enquiry.getApplicantNRIC());
                record.put("ProjectID", enquiry.getProjectID());
                record.put("Query", enquiry.getQuery());
                record.put("Response", enquiry.getResponse() != null ? enquiry.getResponse() : "");
                record.put("Enquiry Status", enquiry.getEnquiryStatus().toString());
                record.put("Enquiry Date", DateTimeUtils.formatDateTime(enquiry.getEnquiryDate()));
                record.put("Last Updated", DateTimeUtils.formatDateTime(enquiry.getLastUpdated()));
                record.put("Responded By", enquiry.getRespondedBy() != null ? enquiry.getRespondedBy() : "");
                records.add(record);
            }

            CsvWriter.write(new EnquiryCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving enquiries: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new EnquiryCsvConfig());
            enquiries = new ArrayList<>();

            for (Map<String, String> record : records) {
                LocalDateTime enquiryDate = DateTimeUtils.parseDateTime(record.get("Enquiry Date"));
                LocalDateTime lastUpdated = DateTimeUtils.parseDateTime(record.get("Last Updated"));
                String response = record.get("Response").trim();
                EnquiryStatus status = EnquiryStatus.valueOf(record.get("Enquiry Status"));
                String respondedBy = record.get("Responded By").trim();

                Enquiry enquiry = new Enquiry(
                    record.get("EnquiryID"),
                    record.get("ApplicantNRIC"),
                    record.get("ProjectID"),
                    record.get("Query"),
                    response,
                    status,
                    enquiryDate,
                    lastUpdated,
                    respondedBy
                );

                enquiries.add(enquiry);
            }
        } catch (IOException e) {
            System.err.println("Error loading enquiries: " + e.getMessage());
        }
    }

    public static List<Enquiry> getAll() {
        return enquiries;
    }

    public static List<Enquiry> getEnquiriesByProject(String projectId) {
        return enquiries.stream()
            .filter(enquiry -> enquiry.getProjectID().equals(projectId))
            .collect(Collectors.toList());
    }

    public static void add(Enquiry enquiry) {
        enquiries.add(enquiry);
        saveAll();
    }
}
