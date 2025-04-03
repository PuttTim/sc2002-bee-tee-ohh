package repositories;

import models.Enquiry;
import utils.CsvReader;
import utils.CsvWriter;
import enums.EnquiryStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                "enquiryId", "applicantNric", "projectId", "query", 
                "response", "enquiryStatus", "enquiryDate", "respondedBy"
            };
        }

        @Override
        public String getFilePath() {
            return "data/sample/EnquiryList.csv";
        }
    }

    private static List<Enquiry> enquiries = new ArrayList<>();

    private EnquiryRepository() {} // private constructor

    public static void saveAll() {
        try {
            List<Map<String, String>> records = new ArrayList<>();

            for (Enquiry enquiry : enquiries) {
                Map<String, String> record = new HashMap<>();
                record.put("enquiryId", enquiry.getEnquiryId());
                record.put("applicantNric", enquiry.getApplicantNric());
                record.put("projectId", enquiry.getProjectId());
                record.put("query", enquiry.getQuery());
                record.put("response", enquiry.getResponse() != null ? enquiry.getResponse() : "");
                record.put("enquiryStatus", enquiry.getEnquiryStatus().toString());
                record.put("enquiryDate", enquiry.getEnquiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                record.put("respondedBy", enquiry.getRespondedBy() != null ? enquiry.getRespondedBy() : "");
                records.add(record);
            }

            CsvWriter.write(new EnquiryCsvConfig(), records);

            System.out.println("Enquiries saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving enquiries: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new EnquiryCsvConfig());
            enquiries = new ArrayList<>();

            for (Map<String, String> record : records) {
                Enquiry enquiry = new Enquiry(
                    record.get("enquiryId"),
                    record.get("applicantNric"),
                    record.get("projectId"),
                    record.get("query"),
                    record.get("respondedBy")
                );

                if (!record.get("response").isEmpty()) {
                    enquiry.markAsResponded(record.get("respondedBy"), record.get("response"));
                }

                enquiry.setEnquiryDate(LocalDateTime.parse(record.get("enquiryDate")));
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
            .filter(enquiry -> enquiry.getProjectId().equals(projectId))
            .collect(Collectors.toList());
    }

    public static void add(Enquiry enquiry) {
        enquiries.add(enquiry);
        saveAll();
    }
}
