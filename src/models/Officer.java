package models;

import java.util.List;

import models.enums.MaritalStatus;
import models.enums.Role;

public class Officer extends Applicant {
    private String currentProjectID;
    private List<String> appliedProjects;

    public Officer(String officerNRIC, String name, String password, int age, List<String> appliedProjects) {
        super(officerNRIC, name, password, age, appliedProjects);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.OFFICER);
        this.currentProjectID = null;
    }

    // Getter
    public String getCurrentProjectID() {
        return currentProjectID;
    }

    // Setter
    public void setCurrentProjectID(String currentProjectID) {
        this.currentProjectID = currentProjectID;
    }

    public List<String> getAppliedProjects() {
        return appliedProjects;
    }

    public void setAppliedProjects(List<String> appliedProjects) {
        this.appliedProjects = appliedProjects;
    }
}