package models;

import models.enums.RegistrationStatus;

/**
 * Represents an officer's registration for a project.
 */
public class OfficerRegistration {
    private final String applicantNric;
    private final Project appliedProject;
    private RegistrationStatus registrationStatus;

    /**
     * Creates an OfficerRegistration object with the given NRIC and project.
     * The registration status is set to PENDING by default.
     *
     * @param applicantNric the NRIC of the officer.
     * @param appliedProject the project the officer is registering for.
     */
    public OfficerRegistration(String applicantNric, Project appliedProject) {
        this.applicantNric = applicantNric;
        this.appliedProject = appliedProject;
        this.registrationStatus = RegistrationStatus.PENDING;
    }

    /**
     * Returns the current registration status of the officer.
     *
     * @return the registration status.
     */
    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }
}
