package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Officer;
import models.Registration;
import models.User;
import models.enums.RegistrationStatus;

import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

/**
 * Repository for managing registrations, including loading from and saving to CSV files.
 * <p>
 * This class provides functionality to load registrations from a CSV file, save them back to the file,
 * and perform CRUD operations such as retrieving registrations by their ID, officer, project, or status.
 * </p>
 */
public class RegistrationRepository {

    /**
     * Configuration for reading and writing the registration CSV file.
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

    private RegistrationRepository() {}

    /**
     * Saves all registrations to the CSV file.
     * <p>
     * The method iterates over all registrations, converts them to a map representation, and writes them
     * to the CSV file using the configured CSV writer.
     * </p>
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
     * Loads registrations from the CSV file.
     * <p>
     * This method reads the CSV file, parses each record, and converts it into a `Registration` object.
     * If any errors occur during parsing, they are logged.
     * </p>
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
     * Retrieves all registrations.
     *
     * @return a list of all registrations
     */
    public static List<Registration> getAll() {
        return registrations;
    }

    /**
     * Adds a new registration to the repository.
     * <p>
     * The method adds the registration to the list and saves it to the CSV file.
     * </p>
     *
     * @param registration the registration to add
     */
    public static void add(Registration registration) {
        registrations.add(registration);
        saveAll();
    }

    /**
     * Retrieves a registration by its ID.
     *
     * @param registrationId the ID of the registration
     * @return the registration with the specified ID, or null if no such registration exists
     */
    public static Registration getById(String registrationId) {
        return registrations.stream()
                .filter(reg -> reg.getRegistrationID().equals(registrationId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all registrations associated with a specific officer.
     *
     * @param officer the officer whose registrations to retrieve
     * @return a list of registrations for the specified officer
     */
    public static List<Registration> getByOfficer(Officer officer) {
        return registrations.stream().filter(reg -> reg.getOfficer().getUserNRIC().equals(officer.getUserNRIC()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all registrations associated with a specific project.
     *
     * @param projectId the ID of the project whose registrations to retrieve
     * @return a list of registrations for the specified project
     */
    public static List<Registration> getByProject(String projectId) {
        return registrations.stream()
                .filter(reg -> reg.getProjectID().equals(projectId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all registrations with a specific registration status.
     *
     * @param status the registration status to filter by
     * @return a list of registrations with the specified status
     */
    public static List<Registration> getByStatus(RegistrationStatus status) {
        return registrations.stream()
                .filter(reg -> reg.getRegistrationStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing registration in the repository.
     * <p>
     * The method searches for the registration by its ID and replaces it with the new one. The updated list is saved to the CSV file.
     * </p>
     *
     * @param registration the registration to update
     */
    public static void update(Registration registration) {
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getRegistrationID().equals(registration.getRegistrationID())) {
                registrations.set(i, registration);
                saveAll();
                return;
            }
        }
    }
}
