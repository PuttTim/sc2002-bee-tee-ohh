package models;

import enums.RegistrationStatus;

public class OfficerRegistration {
    private final String applicantNric;
    private final Project appliedProject;
    private RegistrationStatus registrationStatus;

    public OfficerRegistration(String applicantNric, Project appliedProject) {
        this.applicantNric = applicantNric;
        this.appliedProject = appliedProject;
        this.registrationStatus = RegistrationStatus.PENDING;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }
}
