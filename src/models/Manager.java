package models;

import models.enums.MaritalStatus;
import models.enums.Role;

/**
 * Represents a manager in the system who oversees a project.
 * <p>
 * This class extends the User class and adds functionality specific to a manager,
 * including the management of the current project.
 * </p>
 */
public class Manager extends User {
    private String currentProjectID;

    /**
     * Constructs a Manager object with the provided details.
     *
     * @param managerNRIC the NRIC of the manager
     * @param name the name of the manager
     * @param password the password of the manager
     * @param age the age of the manager
     * @param currentProjectID the ID of the project currently managed by the manager
     */
    public Manager(String managerNRIC, String name, String password, int age, String currentProjectID) {
        super(managerNRIC, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.MANAGER);
        this.currentProjectID = currentProjectID;
    }

    /**
     * Gets the ID of the project currently managed by the manager.
     *
     * @return the current project ID
     */
    public String getCurrentProjectID() {
        return currentProjectID;
    }

    /**
     * Sets the ID of the project to be managed by the manager.
     *
     * @param currentProjectID the project ID to set
     */
    public void setCurrentProjectID(String currentProjectID) {
        this.currentProjectID = currentProjectID;
    }
}
