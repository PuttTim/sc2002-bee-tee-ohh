package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Officer;
import models.Project;
import models.enums.FlatType;

import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

/**
 * Repository class responsible for managing project data.
 * <p>
 * This class handles the loading, saving, and retrieval of project data from and to a CSV file.
 * It supports managing project details, including project ID, manager, flat types, unit availability, application dates, officers, and applicants.
 * </p>
 */
public class ProjectRepository {

    /**
     * Config class for reading and writing project data in CSV format.
     */
    private static class ProjectCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/projects.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("ProjectID", "ManagerNRIC", "ProjectName", "Location",
                    "FlatTypes", "FlatTypeUnits", "FlatTypePrices",
                    "ApplicationOpenDate", "ApplicationCloseDate",
                    "OfficerSlots", "IsVisible", "Applicants", "Officers");
        }
    }

    // List to store projects
    private static List<Project> projects = new ArrayList<>();

    // Private constructor to prevent instantiation
    private ProjectRepository() {}

    /**
     * Saves all projects to the CSV file.
     * <p>
     * Each project is written with its ID, manager NRIC, name, location, flat types, unit details, and other attributes like visibility and application dates.
     * </p>
     */
    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Project project : projects) {
            Map<String, String> record = new HashMap<>();
            record.put("ProjectID", project.getProjectID());
            record.put("ManagerNRIC", project.getManagerNRIC());
            record.put("ProjectName", project.getProjectName());
            record.put("Location", project.getLocation());

            record.put("FlatTypes", project.getFlatTypes().stream()
                    .map(FlatType::toString)
                    .collect(Collectors.joining("/")));

            record.put("FlatTypeUnits", project.getFlatTypeUnits().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("/")));

            record.put("FlatTypePrices", project.getFlatTypeSellingPrice().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("/")));

            record.put("ApplicationOpenDate", DateTimeUtils.formatDateTime(project.getApplicationOpenDate()));
            record.put("ApplicationCloseDate", DateTimeUtils.formatDateTime(project.getApplicationCloseDate()));
            record.put("OfficerSlots", String.valueOf(project.getOfficerSlots()));
            record.put("IsVisible", String.valueOf(project.isVisible()));
            record.put("Applicants", String.join("/", project.getApplicants()));
            record.put("Officers", String.join("/", project.getOfficers()));

            records.add(record);
        }

        try {
            CsvWriter.write(new ProjectCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving projects: " + e.getMessage());
        }
    }

    /**
     * Loads projects from the CSV file and populates the projects list.
     * <p>
     * Each record in the CSV is used to create a project object, associating the project with its flat types, units, prices, officers, and applicants.
     * </p>
     */
    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ProjectCsvConfig());
            projects = new ArrayList<>();

            for (Map<String, String> record : records) {
                List<FlatType> flatTypes = Arrays.stream(record.get("FlatTypes").split("/"))
                        .map(FlatType::valueOf)
                        .collect(Collectors.toList());

                List<Integer> units = Arrays.stream(record.get("FlatTypeUnits").split("/"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                List<Integer> prices = Arrays.stream(record.get("FlatTypePrices").split("/"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                List<String> applicants = new ArrayList<>();
                if (!record.get("Applicants").isEmpty()) {
                    applicants = Arrays.asList(record.get("Applicants").split("/"));
                }

                List<String> officers = new ArrayList<>();
                if (!record.get("Officers").isEmpty()) {
                    officers = Arrays.asList(record.get("Officers").split("/"));
                }

                Project project = new Project(
                        record.get("ProjectID"),
                        record.get("ManagerNRIC"),
                        record.get("ProjectName"),
                        record.get("Location"),
                        flatTypes,
                        units,
                        prices,
                        DateTimeUtils.parseDateTime(record.get("ApplicationOpenDate")),
                        DateTimeUtils.parseDateTime(record.get("ApplicationCloseDate")),
                        Integer.parseInt(record.get("OfficerSlots")),
                        Boolean.parseBoolean(record.get("IsVisible")),
                        applicants,
                        officers
                );

                projects.add(project);
            }
            System.out.println("Loaded " + projects.size() + " projects from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading projects: " + e.getMessage());
        }
    }

    /**
     * Retrieves all projects.
     *
     * @return a list of all projects
     */
    public static List<Project> getAll() {
        return projects;
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param projectId the project ID
     * @return the project with the specified ID, or {@code null} if not found
     */
    public static Project getById(String projectId) {
        return projects.stream()
                .filter(project -> project.getProjectID().equals(projectId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a project by its name.
     *
     * @param projectName the project name
     * @return the project with the specified name, or {@code null} if not found
     */
    public static Project getByName(String projectName) {
        return projects.stream()
                .filter(project -> project.getProjectName().equalsIgnoreCase(projectName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new project to the repository.
     *
     * @param project the project to be added
     */
    public static void add(Project project) {
        projects.add(project);
        saveAll();
    }

    /**
     * Removes a project from the repository by its ID.
     *
     * @param projectId the ID of the project to be removed
     */
    public static void remove(String projectId) {
        projects.removeIf(project -> project.getProjectID().equals(projectId));
        saveAll();
    }

    /**
     * Removes a project from the repository.
     *
     * @param project the project to be removed
     */
    public static void remove(Project project) {
        projects.remove(project);
        saveAll();
    }

    /**
     * Retrieves a list of officer names assigned to a specific project.
     *
     * @param projectID the project ID
     * @return a list of officer names assigned to the specified project
     */
    public static List<String> getProjectOfficerNames(String projectID) {
        Project project = getById(projectID);
        if (project != null) {
            return project.getOfficers().stream()
                    .map(officerNRIC -> OfficerRepository.getByNRIC(officerNRIC))
                    .map(Officer::getName)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Updates an existing project in the repository.
     *
     * @param project the updated project object
     */
    public static void update(Project project) {
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjectID().equals(project.getProjectID())) {
                projects.set(i, project);
                break;
            }
        }
        saveAll();
    }
}
