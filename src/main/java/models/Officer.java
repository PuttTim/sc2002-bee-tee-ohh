package models;

import java.util.List;
import models.enums.MaritalStatus;
import models.enums.Role;

/**
 * Represents an officer in the system who is a type of applicant with additional roles.
 * <p>
 * This class extends the Applicant class, adding specific functionality for officers,
 * including the ability to manage the current project and applied projects.
 * </p>
 */
public class Officer extends Applicant {
    private String currentProjectID;
    private List<String> appliedProjects;

    /**
     * Constructs an Officer object with the provided details.
     *
     * @param officerNRIC the NRIC of the officer
     * @param name the name of the officer
     * @param password the password of the officer
     * @param age the age of the officer
     * @param appliedProjects the list of projects the officer has applied for
     */
    public Officer(String officerNRIC, String name, String password, int age, List<String> appliedProjects) {
        super(officerNRIC, name, password, age, appliedProjects);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.OFFICER);
        this.currentProjectID = null;
    }

    /**
     * Gets the ID of the project currently managed by the officer.
     *
     * @return the current project ID
     */
    public String getCurrentProjectID() {
        return currentProjectID;
    }

    /**
     * Sets the ID of the project to be managed by the officer.
     *
     * @param currentProjectID the project ID to set
     */
    public void setCurrentProjectID(String currentProjectID) {
        this.currentProjectID = currentProjectID;
    }

    /**
     * Gets the list of projects that the officer has applied for.
     *
     * @return the list of applied projects
     */
    public List<String> getAppliedProjects() {
        return appliedProjects;
    }

    /**
     * Sets the list of projects that the officer has applied for.
     *
     * @param appliedProjects the list of projects to set
     */
    public void setAppliedProjects(List<String> appliedProjects) {
        this.appliedProjects = appliedProjects;
    }
}
