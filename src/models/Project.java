package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import enums.FlatType;

public class Project {
    private static int lastProjectId = 0;

    private String projectId;
    private String managerNric;
    private String projectName;
    private String location;
    private FlatType flatType;
    private int numberOfFlats;
    private LocalDateTime applicationStart;
    private LocalDateTime applicationEnd;
    private int availableOfficerSlots;
    private boolean isVisible;
    private List<String> applicantNrics;
    private List<String> officerNrics;

    public Project(String managerNric, String projectName, String location,
                   FlatType flatType, int numberOfFlats,
                   LocalDateTime applicationStart, LocalDateTime applicationEnd,
                   int availableOfficerSlots, boolean isVisible,
                   List<String> applicantNrics, List<String> officerNrics) {

        this.projectId = "P" + (++Project.lastProjectId);
        this.managerNric = managerNric;
        this.projectName = projectName;
        this.location = location;
        this.flatType = flatType;
        this.numberOfFlats = numberOfFlats;
        this.applicationStart = applicationStart;
        this.applicationEnd = applicationEnd;
        this.availableOfficerSlots = availableOfficerSlots;
        this.isVisible = isVisible;
        this.applicantNrics = applicantNrics != null ? applicantNrics : new ArrayList<>();
        this.officerNrics = officerNrics != null ? officerNrics : new ArrayList<>();
    }

    public Project(String projectId, String managerNric, String projectName, String location,
                   FlatType flatType, int numberOfFlats,
                   LocalDateTime applicationStart, LocalDateTime applicationEnd,
                   int availableOfficerSlots, boolean isVisible,
                   List<String> applicantNrics, List<String> officerNrics) {

        this.projectId = projectId;
        this.managerNric = managerNric;
        this.projectName = projectName;
        this.location = location;
        this.flatType = flatType;
        this.numberOfFlats = numberOfFlats;
        this.applicationStart = applicationStart;
        this.applicationEnd = applicationEnd;
        this.availableOfficerSlots = availableOfficerSlots;
        this.isVisible = isVisible;
        this.applicantNrics = applicantNrics != null ? applicantNrics : new ArrayList<>();
        this.officerNrics = officerNrics != null ? officerNrics : new ArrayList<>();

        try {
            int numericId = Integer.parseInt(projectId.replaceAll("\\D+", ""));
            if (numericId > Project.lastProjectId) {
                Project.lastProjectId = numericId;
            }
        } catch (NumberFormatException e) {
        }
    }

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public String getManagerNric() {
        return managerNric;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getLocation() {
        return location;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public int getNumberOfFlats() {
        return numberOfFlats;
    }

    public LocalDateTime getApplicationStart() {
        return applicationStart;
    }

    public LocalDateTime getApplicationEnd() {
        return applicationEnd;
    }

    public int getAvailableOfficerSlots() {
        return availableOfficerSlots;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public List<String> getApplicantNrics() {
        return applicantNrics;
    }

    public List<String> getOfficerNrics() {
        return officerNrics;
    }

    // Setters
    public void setManagerNric(String managerNric) {
        this.managerNric = managerNric;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public void setNumberOfFlats(int numberOfFlats) {
        this.numberOfFlats = numberOfFlats;
    }

    public void setApplicationStart(LocalDateTime applicationStart) {
        this.applicationStart = applicationStart;
    }

    public void setApplicationEnd(LocalDateTime applicationEnd) {
        this.applicationEnd = applicationEnd;
    }

    public void setAvailableOfficerSlots(int availableOfficerSlots) {
        this.availableOfficerSlots = availableOfficerSlots;
    }

    public void setVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setApplicantNrics(List<String> applicantNrics) {
        this.applicantNrics = applicantNrics != null ? applicantNrics : new ArrayList<>();
    }

    public void setOfficerNrics(List<String> officerNrics) {
        this.officerNrics = officerNrics != null ? officerNrics : new ArrayList<>();
    }

    // Helpers
    public boolean isApplicationOpen(LocalDateTime now) {
        return now.isAfter(applicationStart) && now.isBefore(applicationEnd);
    }

    public void addApplicant(String applicantNric) {
        if (!applicantNrics.contains(applicantNric)) {
            applicantNrics.add(applicantNric);
        }
    }

    public void addOfficer(String officerNric) {
        if (!officerNrics.contains(officerNric)) {
            officerNrics.add(officerNric);
        }
    }

    public void reduceOfficerSlot() {
        if (availableOfficerSlots > 0) {
            availableOfficerSlots--;
        }
    }

    public void reduceFlatCount() {
        if (numberOfFlats > 0) {
            numberOfFlats--;
        }
    }
}