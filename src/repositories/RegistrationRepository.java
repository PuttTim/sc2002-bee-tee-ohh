package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Registration;
import models.User;
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

/**
 * A repository class for managing officer registrations for projects.
 * Supports loading and saving data to CSV and searching registrations by different criteria.
 */
public class RegistrationRepository {

    /**
     * Configuration for reading and writing the CSV file containing registrations.
     */
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

    /**
     * Saves all registrations to the CSV file.
     */
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
            record.put("ApprovedBy", registration.getApprovedBy() != null ? registration.getApprovedBy().getUserNRIC() : "");
            records.add(record);
        }

        try {
            CsvWriter.write(new RegistrationCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving registrations: " + e.getMessage());
        }
    }

    /**
     * Loads all registrations from the CSV file.
     */
    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new RegistrationCsvConfig());
            registrations = new ArrayList<>();

            for (Map<String, String> record : records) {
                User officer = UserRepository.getByNRIC(record.get("OfficerNRIC"));
                User approver = record.get("ApprovedBy") != null && !record.get("ApprovedBy").isEmpty() ?
                        UserRepository.getByNRIC(record.get("ApprovedBy")) : null;

                if (officer == null) {
                    System.err.println("Officer NRIC not found in users.csv: " + record.get("OfficerNRIC"));
                    continue;
                }

                Registration registration = new Registration(
                        record.get("RegistrationID"),
                        officer,
                        record.get("ProjectID"),
                        RegistrationStatus.valueOf(record.get("RegistrationStatus")),
                        DateTimeUtils.parseDateTime(record.get("RegistrationDate")),
                        DateTimeUtils.parseDateTime(record.get("LastUpdated")),
                        approver
                );
                registrations.add(registration);
            }
            System.out.println("Loaded " + registrations.size() + " registrations from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading registrations: " + e.getMessage());
        }
    }

    /**
     * Returns all registrations in the system.
     *
     * @return list of all registrations
     */
    public static List<Registration> getAll() {
        return registrations;
    }

    /**
     * Adds a new registration to the repository.
     *
     * @param registration the registration to add
     */
    public static void add(Registration registration) {
        registrations.add(registration);
        saveAll();
    }

    /**
     * Finds a registration by its registration ID.
     *
     * @param registrationId the ID of the registration to search for
     * @return the matching registration, or <code>null</code> if not found
     */
    public static Registration getById(String registrationId) {
        return registrations.stream()
                .filter(reg -> reg.getRegistrationID().equals(registrationId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds all registrations for a given officer.
     *
     * @param officer the officer whose registrations to search for
     * @return list of registrations for the officer
     */
    public static List<Registration> getByOfficer(Officer officer) {
        return registrations.stream().filter(reg -> reg.getOfficer().getUserNRIC().equals(officer.getUserNRIC()))
                .collect(Collectors.toList());
    }

    /**
     * Finds all registrations for a given project.
     *
     * @param projectId the ID of the project whose registrations to search for
     * @return list of registrations for the project
     */
    public static List<Registration> getByProject(String projectId) {
        return registrations.stream()
                .filter(reg -> reg.getProjectID().equals(projectId))
                .collect(Collectors.toList());
    }

    /**
     * Finds all registrations with a given status.
     *
     * @param status the status of the registrations to search for
     * @return list of registrations with the specified status
     */
    public static List<Registration> getByStatus(RegistrationStatus status) {
        return registrations.stream()
                .filter(reg -> reg.getRegistrationStatus() == status)
                .collect(Collectors.toList());
    }
}