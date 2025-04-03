package models;

import java.time.LocalDateTime;
import java.util.List;

import enums.FlatType;

import java.util.ArrayList;

public class Project {
    private String projectId;
    private String managerNric;
    private String projectName;
    private String neighbourhood;
    private FlatType flatType;
    private int availableUnits;
    private int unitPrice; 
    private LocalDateTime applicationStart;
    private LocalDateTime applicationEnd;
    private int availableOfficerSlots;
    private boolean isVisible;
    private List<String> applicants;
    private List<String> officers;

    public Project(String projectId, String managerNric, String projectName, String neighbourhood, FlatType flatType, int availableUnits, int unitPrice, LocalDateTime applicationStart, LocalDateTime applicationEnd, int availableOfficerSlots, boolean isVisible) {
        this.projectId = projectId;
        this.managerNric = managerNric;
        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.flatType = flatType;
        this.availableUnits = availableUnits;
        this.unitPrice = unitPrice;
        this.applicationStart = applicationStart;
        this.applicationEnd = applicationEnd;
        this.availableOfficerSlots = availableOfficerSlots;
        this.isVisible = isVisible;
        this.applicants = new ArrayList<>();
        this.officers = new ArrayList<>();
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
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setManagerNric(String managerNric) {
        this.managerNric = managerNric;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public void addApplicant(String applicantNric) {
        this.applicants.add(applicantNric);
    }

    public void addOfficer(String officerNric) {
        this.officers.add(officerNric);
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public int getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setApplicants(List<String> applicants) {
        this.applicants = applicants;
    }

    public void setOfficers(List<String> officers) {
        this.officers = officers;
    }
}