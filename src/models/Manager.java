package models;

import enums.MaritalStatus;
import enums.Role;

public class Manager extends User {
    private String currentProjectId;

    public Manager(String managerNric, String name, String password, int age, String currentProjectId) {
        super(managerNric, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.MANAGER);
        this.currentProjectId = currentProjectId;
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