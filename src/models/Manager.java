package models;

import enums.MaritalStatus;
import enums.Role;

public class Manager extends User {
    private String currentProjectID;

    public Manager(String managerNRIC, String name, String password, int age, String currentProjectID) {
        super(managerNRIC, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.MANAGER);
        this.currentProjectID = currentProjectID;
    }

    // Getter
    public String getCurrentProjectID() {
        return currentProjectID;
    }

    // Setter
    public void setCurrentProjectID(String currentProjectID) {
        this.currentProjectID = currentProjectID;
    }
}