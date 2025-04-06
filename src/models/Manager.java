package models;

import enums.MaritalStatus;
import enums.Role;

public class Manager extends User {
    private String currentProjectId;

    public Manager(String managerNric, String name, String password, int age, String currentProjectId) {
        super(managerNric, name, password, age, Role.MANAGER, MaritalStatus.SINGLE);
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