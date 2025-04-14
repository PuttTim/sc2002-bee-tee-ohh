package repositories;

import models.Registration;
import models.enums.RegistrationStatus;
import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.ICsvConfig;

public class RegistrationRepository {
    private static class RegistrationCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/registration.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("RegistrationID", "OfficerNRIC", "ProjectID", "RegistrationStatus",
                          "RegistrationDate", "LastUpdated", "ApprovedBy");
        }
    }

    private static List<Registration> registrations = new ArrayList<>();

    private RegistrationRepository() {} // private constructor

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Registration registration : registrations) {
            Map<String, String> record = new HashMap<>();
            record.put("RegistrationID", registration.getRegistrationID());
            record.put("OfficerNRIC", registration.getOfficer().getUserNRIC());
            record.put("ProjectID", registration.getProjectID());
            record.put("RegistrationStatus", registration.getRegistrationStatus().toString());
            record.put("RegistrationDate", DateTimeUtils.formatDateTime(registration.getRegistrationDate()));
            record.put("LastUpdated", DateTimeUtils.formatDateTime(registration.getLastUpdated()));
            record.put("ApprovedBy", registration.getApprovedBy().getUserNRIC());
            records.add(record);
        }

        try {
            CsvWriter.write(new RegistrationCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving registrations: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new RegistrationCsvConfig());
            registrations = new ArrayList<>();

            for (Map<String, String> record : records) {
                Registration registration = new Registration(
                    record.get("RegistrationID"),
                    UserRepository.getByNRIC(record.get("OfficerNRIC")),
                    record.get("ProjectID"),
                    RegistrationStatus.valueOf(record.get("RegistrationStatus")),
                    DateTimeUtils.parseDateTime(record.get("RegistrationDate")),
                    DateTimeUtils.parseDateTime(record.get("LastUpdated")),
                    UserRepository.getByNRIC(record.get("ApprovedBy"))
                );
                registrations.add(registration);
            }

            System.out.println("Loaded " + registrations.size() + " registrations from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading registrations: " + e.getMessage());
        }
    }

    public static List<Registration> getAll() {
        return registrations;
    }

    public static void add(Registration registration) {
        registrations.add(registration);
    }

    public static Registration getById(String registrationId) {
        return registrations.stream()
            .filter(reg -> reg.getRegistrationID().equals(registrationId))
            .findFirst()
            .orElse(null);
    }

    public static List<Registration> getByOfficer(String officerNRIC) {
        return registrations.stream()
            .filter(reg -> reg.getOfficer().getUserNRIC().equals(officerNRIC))
            .collect(Collectors.toList());
    }

    public static List<Registration> getByProject(String projectId) {
        return registrations.stream()
            .filter(reg -> reg.getProjectID().equals(projectId))
            .collect(Collectors.toList());
    }

    public static List<Registration> getByStatus(RegistrationStatus status) {
        return registrations.stream()
            .filter(reg -> reg.getRegistrationStatus() == status)
            .collect(Collectors.toList());
    }
}