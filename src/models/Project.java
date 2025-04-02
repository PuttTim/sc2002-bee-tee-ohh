package models;

import java.time.LocalDate;

public class Project {
    private String projectId;
    private Manager managerNric;
    private String projectName;
    private String location;
    private LocalDate applicationStart;
    private LocalDate applicationEnd;
    private int officerSlots;
    private boolean visibility;


    // Might need to add minimal constructor
    public Project (String projectId, Manager managerNric, String projectName, String location,
    LocalDate applicationStart, LocalDate applicationEnd, int officerSlots, boolean visibility) {
        this.projectId = projectId;
        this.managerNric = managerNric;
        this.projectName = projectName;
        this.location = location;
        this.applicationStart = applicationStart;
        this.applicationEnd = applicationEnd;
        this.officerSlots = 10;
        this.visibility = false; // Might need to add array of officers and applicantNrics instead
    }

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public Manager getManagerNric() {
        return managerNric;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getApplicationStart() {
        return applicationStart;
    }

    public LocalDate getApplicationEnd() {
        return applicationEnd;
    }

    public int getOfficerSlots() {
        return officerSlots;
    }

    public boolean isVisible() {
        return visibility;
    }
    
    // Setters
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setManagerNric(Manager managerNric) {
        this.managerNric = managerNric;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setApplicationStart(LocalDate applicationStart) {
        this.applicationStart = applicationStart;
    }

    public void setApplicationEnd(LocalDate applicationEnd) {
        this.applicationEnd = applicationEnd;
    }

    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
