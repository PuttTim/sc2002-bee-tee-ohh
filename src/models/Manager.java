package models;

import models.enums.MaritalStatus;
import models.enums.Role;

/**
 * Represents a manager.
 * A manager is a user with a current project ID.
 */
public class Manager extends User {
    private String currentProjectID;

    /**
     * Creates a Manager object with the given details.
     *
     * @param managerNRIC the NRIC of the manager.
     * @param name the name of the manager.
     * @param password the password of the manager.
     * @param age the age of the manager.
     * @param currentProjectID the project ID the manager is currently assigned to.
     */
    public Manager(String managerNRIC, String name, String password, int age, String currentProjectID) {
        super(managerNRIC, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.MANAGER);
        this.currentProjectID = currentProjectID;
    }

    // Getter
    /**
     * Gets current project ID of the manager.
     *
     * @return the current project ID.
     */
    public String getCurrentProjectID() {
        return currentProjectID;
    }

    // Setter
    /**
     * Sets current project ID of the manager.
     *
     * @param currentProjectID the project ID to set.
     */
    public void setCurrentProjectID(String currentProjectID) {
        this.currentProjectID = currentProjectID;
    }
}