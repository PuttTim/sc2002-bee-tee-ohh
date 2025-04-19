package models;

/**
 * Represents an application made by an officer to a specific project.
 */
public class OfficerApplication {
    private final String applicantNric;
    private final Project appliedProject;
    private String applicationOutcome;

    /**
     * Creates an OfficerApplication object with the given NRIC and project.
     * The application outcome is set to "Pending" by default.
     *
     * @param applicantNric the NRIC of the officer who is applying.
     * @param appliedProject the project the officer applied to.
     */
    public OfficerApplication(String applicantNric, Project appliedProject) {
        this.applicantNric = applicantNric;
        this.appliedProject = appliedProject;
        this.applicationOutcome = "Pending";
    }

    /**
     * Returns the outcome of the officer's application.
     *
     * @return the application outcome.
     */
    public String getApplicationOutcome() {
        return applicationOutcome;
    }
}
