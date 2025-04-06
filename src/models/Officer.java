package models;

import enums.MaritalStatus;
import enums.Role;

public class Officer extends User {
    private String currentProjectId;

    public Officer(String officerNric, String name, String password, int age, String currentProjectId) {
        super(officerNric, name, password, age, Role.OFFICER, MaritalStatus.SINGLE);
        this.currentProjectId = currentProjectId;
    }

    // Getters
    public String getCurrentProjectId() {
        return currentProjectId;
    }

    // Setters
    public void setCurrentProjectId(String currentProjectId) {
        this.currentProjectId = currentProjectId;
    }
}
