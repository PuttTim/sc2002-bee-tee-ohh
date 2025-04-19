package repositories;

import models.Officer;
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

    private static List<Project> projects = new ArrayList<>();

    private ProjectRepository() {
    }

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

    public static List<Project> getAll() {
        return projects;
    }

    public static Project getById(String projectId) {
        return projects.stream()
            .filter(project -> project.getProjectID().equals(projectId))
            .findFirst()
            .orElse(null);
    }

    public static Project getByName(String projectName) {
        return projects.stream()
            .filter(project -> project.getProjectName().equalsIgnoreCase(projectName))
            .findFirst()
            .orElse(null);
    }

    public static void add(Project project) {
        projects.add(project);
        saveAll();
    }

    public static void remove(String projectId) {
        projects.removeIf(project -> project.getProjectID().equals(projectId));
        saveAll();
    }

    public static void remove(Project project) {
        projects.remove(project);
        saveAll();
    }

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
}
