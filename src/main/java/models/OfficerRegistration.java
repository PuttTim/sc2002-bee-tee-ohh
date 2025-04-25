package models;

import models.enums.RegistrationStatus;

/**
 * Represents an officer's registration for a project.
 * <p>
 * This class holds information about the officer's registration status, including the applicant's
 * NRIC, the project they are applying for, and their registration status.
 * </p>
 */
public class OfficerRegistration {
    private final String applicantNric;
    private final Project appliedProject;
    private RegistrationStatus registrationStatus;

    /**
     * Constructs an OfficerRegistration with the specified officer's NRIC and applied project.
     * The registration status is initially set to "PENDING".
     *
     * @param applicantNric the NRIC of the officer registering
     * @param appliedProject the project the officer is registering for
     */
    public OfficerRegistration(String applicantNric, Project appliedProject) {
        this.applicantNric = applicantNric;
        this.appliedProject = appliedProject;
        this.registrationStatus = RegistrationStatus.PENDING;
    }

    /**
     * Gets the current registration status of the officer.
     *
     * @return the registration status (e.g., PENDING, APPROVED, REJECTED)
     */
    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }
}
