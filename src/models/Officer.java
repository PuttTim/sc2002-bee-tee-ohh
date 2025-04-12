package models;

import models.enums.MaritalStatus;
import models.enums.Role;

public class Officer extends User {
    private String currentProjectID;

    public Officer(String officerNRIC, String name, String password, int age) {
        super(officerNRIC, name, password, age);
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
}