package models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Project {
    private String projectId;
    private String managerNric;
    private String projectName;
    private String location;
    private LocalDateTime applicationStart;
    private LocalDateTime applicationEnd;
    private int availableOfficerSlots;
    private boolean isVisible;
    private List<String> applicants;
    private List<String> officers;

    public Project(String projectId, String managerNric, String projectName, String location,
                   LocalDateTime applicationStart, LocalDateTime applicationEnd,
                   int availableOfficerSlots, boolean isVisible,
                   List<String> applicants, List<String> officers) {
        this.projectId = projectId;
        this.managerNric = managerNric;
        this.projectName = projectName;
        this.location = location;
        this.applicationStart = applicationStart;
        this.applicationEnd = applicationEnd;
        this.availableOfficerSlots = availableOfficerSlots;
        this.isVisible = isVisible;
        this.applicants = applicants != null ? applicants : new ArrayList<>();
        this.officers = officers != null ? officers : new ArrayList<>();
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

    public List<String> getApplicants() {
        return applicants;
    }

    public List<String> getOfficers() {
        return officers;
    }

    // Setters
    public void setApplicants(List<String> applicants) {
        this.applicants = applicants != null ? applicants : new ArrayList<>();
    }

    public void setOfficers(List<String> officers) {
        this.officers = officers != null ? officers : new ArrayList<>();
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setManagerNric(String managerNric) {
        this.managerNric = managerNric;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setLocation(String location) {
        this.location = location;
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

    // Helpers
    public void addApplicant(String applicantNric) {
        if (!applicants.contains(applicantNric)) {
            applicants.add(applicantNric);
        }
    }

    public void addOfficer(String officerNric) {
        if (!officers.contains(officerNric)) {
            officers.add(officerNric);
        }
    }
}