package models;

import enums.MaritalStatus;
import enums.Role;

public class Officer extends User {
    private String currentProjectId;

    public Officer(String officerNric, String name, String password, int age) {
        super(officerNric, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.OFFICER);
        this.currentProjectId = null;
    }

    // Getter
    public String getCurrentProjectId() {
        return currentProjectId;
    }

    // Setter
    public void setCurrentProjectId(String currentProjectId) {
        this.currentProjectId = currentProjectId;
    }
}