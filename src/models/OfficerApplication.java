package models;

public class OfficerApplication {
    private final String applicantNric;
    private final Project appliedProject;
    private String applicationOutcome;

    public OfficerApplication(String applicantNric, Project appliedProject) {
        this.applicantNric = applicantNric;
        this.appliedProject = appliedProject;
        this.applicationOutcome = "Pending";
    }

    public String getApplicationOutcome() {
        return applicationOutcome;
    }
}
