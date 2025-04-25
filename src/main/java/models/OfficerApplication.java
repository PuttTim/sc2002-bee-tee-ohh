package models;

/**
 * Represents an officer's application for a project.
 * <p>
 * This class holds information about an officer's application, including the applicant's NRIC,
 * the project they applied for, and the outcome of their application.
 * </p>
 */
public class OfficerApplication {
    private final String applicantNric;
    private final Project appliedProject;
    private String applicationOutcome;

    /**
     * Constructs an OfficerApplication with the specified officer's NRIC and applied project.
     * The application outcome is initially set to "Pending".
     *
     * @param applicantNric the NRIC of the officer applying
     * @param appliedProject the project the officer is applying for
     */
    public OfficerApplication(String applicantNric, Project appliedProject) {
        this.applicantNric = applicantNric;
        this.appliedProject = appliedProject;
        this.applicationOutcome = "Pending";
    }

    /**
     * Gets the outcome of the officer's application.
     *
     * @return the application outcome (e.g., "Pending", "Approved", etc.)
     */
    public String getApplicationOutcome() {
        return applicationOutcome;
    }
}
