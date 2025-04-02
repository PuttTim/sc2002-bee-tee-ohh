package models;

import enums.MaritalStatus;
import enums.Role;

public class Officer extends User {
    private Project projectId;

    public Officer(String officerNric, String name, String password, int age,  Project projectId) {
        super(officerNric, name, password, age, Role.OFFICER, MaritalStatus.SINGLE);
        this.projectId = null;
    }

    // Getters
    public Project getProjectId() {
        return projectId;
    }

    // Setters
    public void setProject(Project projectId) {
        this.projectId = projectId;
    }
}
