package repositories;

import models.Project;
import utils.CsvReader;
import utils.CsvWriter;
import enums.FlatType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import interfaces.ICsvConfig;

public class ProjectRepository {
    private static class ProjectCsvConfig implements ICsvConfig {
        @Override
        public String[] getHeaders() {
            return new String[] {
                "projectId","projectName","neighbourhood","flatType","availableUnits",
                "unitPrice","applicationStart","applicationEnd","managerNric",
                "availableOfficerSlots","officers"
            };
        }

        @Override
        public String getFilePath() {
            return "data/sample/ProjectList.csv";
        }
    }

    // private so that we don't accidentaly initialise it
    private ProjectRepository() {
    }

    private static List<Project> projects = new ArrayList<>();

    public static void saveAll() {
        // Save all projects to the CSV file
        try {
            List<Map<String, String>> records = new ArrayList<>();

            for (Project project : projects) {
                Map<String, String> record = new HashMap<>();

                record.put("projectId", project.getProjectId());
                record.put("projectName", project.getProjectName());
                record.put("neighbourhood", project.getNeighbourhood());
                record.put("flatType", project.getFlatType().toString().replace("_", ""));
                record.put("availableUnits", String.valueOf(project.getAvailableUnits()));
                record.put("unitPrice", String.valueOf(project.getUnitPrice()));
                record.put("applicationStart", project.getApplicationStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                record.put("applicationEnd", project.getApplicationEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                record.put("managerNric", project.getManagerNric());
                record.put("availableOfficerSlots", String.valueOf(project.getAvailableOfficerSlots()));
                record.put("officers", String.join("/", project.getOfficers()));
                records.add(record);
            }

            CsvWriter.write(new ProjectCsvConfig(), records);

            System.out.println("Projects saved successfully.");
        } catch (IOException e) {
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ProjectCsvConfig());
            projects = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Map<String, String> record : records) {
                Project project = new Project(
                    record.get("projectId"),
                    record.get("managerNric"),
                    record.get("projectName"),
                    record.get("neighbourhood"),
                    FlatType.valueOf("_" +record.get("flatType")),
                    Integer.parseInt(record.get("availableUnits")),
                    Integer.parseInt(record.get("unitPrice")),
                    LocalDate.parse(record.get("applicationStart"), formatter).atStartOfDay(),
                    LocalDate.parse(record.get("applicationEnd"), formatter).atStartOfDay(),
                    Integer.parseInt(record.get("availableOfficerSlots")),
                    true // default to visible
                );

                if (record.get("officers") != null && !record.get("officers").isEmpty()) {
                    project.setOfficers(Arrays.asList(record.get("officers").split("/")));
                    System.out.println(record.get("officers") + "Officer");
                }

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
        projects.removeIf(project -> project.getProjectId().equals(projectId));
    }

    public static Project getById(String projectId) {
        for (Project project : projects) {
            if (project.getProjectId().equals(projectId)) {
                return project;
            }
        }

        return null;
    }
}
