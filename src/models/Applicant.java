package models;

import java.util.ArrayList;
import java.util.List;

import models.enums.MaritalStatus;
import models.enums.Role;
import models.enums.FlatType;

public class Applicant extends User {
    private List<String> appliedProjects;
    private FlatType bookedFlatType;
    private String bookedProjectId;

    public Applicant(String applicantNRIC, String name, String password, int age, List<String> appliedProjects) {
        super(applicantNRIC, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.APPLICANT);
        this.appliedProjects = appliedProjects != null ? appliedProjects : new ArrayList<>();
        this.bookedFlatType = null;
        this.bookedProjectId = null;
    }

    // Getters
    public List<String> getAppliedProjects() {
        return new ArrayList<>(appliedProjects);
    }

    public FlatType getBookedFlatType() {
        return bookedFlatType;
    }

    public String getBookedProjectId() {
        return bookedProjectId;
    }

    public boolean hasBookedFlat() {
        return bookedProjectId != null && bookedFlatType != null;
    }

    // Setters
    public void setAppliedProjects(List<String> projects) {
        this.appliedProjects = projects != null ? new ArrayList<>(projects) : new ArrayList<>();
    }

    public void setBookedFlat(String projectId, FlatType flatType) {
        this.bookedProjectId = projectId;
        this.bookedFlatType = flatType;
    }

    // Helper
    public void addAppliedProject(String projectId) {
        if (projectId != null && !appliedProjects.contains(projectId)) {
            appliedProjects.add(projectId);
        }
    }

    public void removeAppliedProject(String projectId) {
        appliedProjects.remove(projectId);
    }

    public void clearBookedFlat() {
        this.bookedProjectId = null;
        this.bookedFlatType = null;
    }
}
