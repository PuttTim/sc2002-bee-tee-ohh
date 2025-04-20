package models;

import java.util.List;
import models.enums.MaritalStatus;
import models.enums.Role;

/**
 * Represents an officer.
 * An officer is a type of applicant with a current project ID and applied projects.
 */
public class Officer extends Applicant {
    private String currentProjectID;
    private List<String> appliedProjects;

    /**
     * Creates an Officer object with the given details.
     *
     * @param officerNRIC the NRIC of the officer.
     * @param name the name of the officer.
     * @param password the password of the officer.
     * @param age the age of the officer.
     * @param appliedProjects the list of project IDs the officer has applied to.
     */
    public Officer(String officerNRIC, String name, String password, int age, List<String> appliedProjects) {
        super(officerNRIC, name, password, age, appliedProjects);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.OFFICER);
        this.currentProjectID = null;
    }

    // Getter
    /**
     * Gets current project ID of the officer.
     *
     * @return the current project ID.
     */
    public String getCurrentProjectID() {
        return currentProjectID;
    }

    /**
     * Gets a list of project IDs the officer has applied to.
     *
     * @return the list of applied project IDs.
     */
    public List<String> getAppliedProjects() {
        return appliedProjects;
    }

    // Setter
    /**
     * Sets the current project ID for the officer.
     *
     * @param currentProjectID the project ID to set.
     */
    public void setCurrentProjectID(String currentProjectID) {
        this.currentProjectID = currentProjectID;
    }

    /**
     * Sets the list of applied project IDs for the officer.
     *
     * @param appliedProjects the list of applied project IDs to set.
     */
    public void setAppliedProjects(List<String> appliedProjects) {
        this.appliedProjects = appliedProjects;
    }
}