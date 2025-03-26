package models;

import enums.MaritalStatus;
import enums.Role;

public class Officer extends Applicant {
    private Project handledProject;
    private OfficerApplication officerApplication;

    public Officer(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.handledProject = null;
    }

    // Getters
    public Project getHandledProject() {
        return handledProject;
    }

    public OfficerApplication getOfficerApplication() {
        return officerApplication;
    }

    // Setters
    public void setHandledProject(Project project) {
        this.handledProject = project;
    }

    public void setOfficerApplication(OfficerApplication officerApplication) {
        this.officerApplication = officerApplication;
    }
}
