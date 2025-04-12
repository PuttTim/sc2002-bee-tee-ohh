package repositories;

import models.Project;
import models.enums.FlatType;
import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.ICsvConfig;

public class ProjectRepository {
    private static class ProjectCsvConfig implements ICsvConfig {
        @Override
        public String[] getHeaders() {
            return new String[] {
                "ProjectID", "ManagerNRIC", "Project Name", "Location", 
                "Flat Types", "Flat Type Units", "Flat Type Prices",
                "Application Open Date", "Application Close Date",
                "Officer Slots", "Is Visible", "Applicants", "Officers"
            };
        }

        @Override
        public String getFilePath() {
            return "data/projects.csv";
        }
    }

    private static List<Project> projects = new ArrayList<>();

    private ProjectRepository() {
    }

    public static void saveAll() {
        try {
            List<Map<String, String>> records = new ArrayList<>();

            for (Project project : projects) {
                Map<String, String> record = new HashMap<>();

                record.put("ProjectID", project.getProjectID());
                record.put("ManagerNRIC", project.getManagerNRIC());
                record.put("Project Name", project.getProjectName());
                record.put("Location", project.getLocation());
                
                // Convert flat types to string
                String flatTypes = project.getFlatTypes().stream()
                    .map(FlatType::toString)
                    .collect(Collectors.joining("/"));
                record.put("Flat Types", flatTypes);
                
                // Convert units to string
                String units = project.getFlatTypeUnits().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("/"));
                record.put("Flat Type Units", units);
                
                // Convert prices to string
                String prices = project.getFlatTypeSellingPrice().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("/"));
                record.put("Flat Type Prices", prices);

                record.put("Application Open Date", DateTimeUtils.formatDateTime(project.getApplicationOpenDate()));
                record.put("Application Close Date", DateTimeUtils.formatDateTime(project.getApplicationCloseDate()));
                record.put("Officer Slots", String.valueOf(project.getOfficerSlots()));
                record.put("Is Visible", String.valueOf(project.isVisible()));
                record.put("Applicants", String.join("/", project.getApplicants()));
                record.put("Officers", String.join("/", project.getOfficers()));

                records.add(record);
            }

            CsvWriter.write(new ProjectCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving projects: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ProjectCsvConfig());
            projects = new ArrayList<>();

            for (Map<String, String> record : records) {
                // Parse flat types
                List<FlatType> flatTypes = Arrays.stream(record.get("Flat Types").split("/"))
                    .map(FlatType::valueOf)
                    .collect(Collectors.toList());
                
                // Parse units
                List<Integer> units = Arrays.stream(record.get("Flat Type Units").split("/"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
                
                // Parse prices
                List<Integer> prices = Arrays.stream(record.get("Flat Type Prices").split("/"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

                // Parse applicants and officers
                List<String> applicants = new ArrayList<>();
                if (record.get("Applicants") != null && !record.get("Applicants").isEmpty()) {
                    applicants = Arrays.asList(record.get("Applicants").split("/"));
                }

                List<String> officers = new ArrayList<>();
                if (record.get("Officers") != null && !record.get("Officers").isEmpty()) {
                    officers = Arrays.asList(record.get("Officers").split("/"));
                }

                Project project = new Project(
                    record.get("ProjectID"),
                    record.get("ManagerNRIC"),
                    record.get("Project Name"),
                    record.get("Location"),
                    flatTypes,
                    units,
                    prices,
                    DateTimeUtils.parseDateTime(record.get("Application Open Date")),
                    DateTimeUtils.parseDateTime(record.get("Application Close Date")),
                    Integer.parseInt(record.get("Officer Slots")),
                    Boolean.parseBoolean(record.get("Is Visible")),
                    applicants,
                    officers
                );

                projects.add(project);
            }
        } catch (IOException e) {
            System.err.println("Error loading projects: " + e.getMessage());
        }
    }

    public static List<Project> getAll() {
        return projects;
    }

    public static void add(Project project) {
        projects.add(project);
    }

    public static void remove(String projectId) {
        projects.removeIf(project -> project.getProjectID().equals(projectId));
    }

    public static Project getById(String projectId) {
        return projects.stream()
            .filter(project -> project.getProjectID().equals(projectId))
            .findFirst()
            .orElse(null);
    }
}
