package interfaces;

import models.Officer;
import models.OfficerRegistration;
import models.Project;
import models.enums.RegistrationStatus;

/**
 * Interface for managing officer registrations.
 */
public interface IOfficerRegistrationService {

    /**
     * Creates a new registration, for an officer and a project.
     *
     * @param officer the officer to register for a project.
     * @param project the project the officer is registering for.
     * @return the created officer registration.
     */
    public OfficerRegistration createRegistration(Officer officer, Project project);

    /**
     * Saves an officer registration.
     *
     * @param officerRegistration the registration to be saved.
     */
    public void saveRegistration(OfficerRegistration officerRegistration);

    /**
     * Gets the registration status for an officer.
     *
     * @param officer the officer whose registration status is to be retrieved.
     * @return the registration status of the officer.
     */
    public RegistrationStatus getRegistrationStatus(Officer officer);
}

