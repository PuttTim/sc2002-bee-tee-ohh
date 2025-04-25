package interfaces;

import models.enums.RegistrationStatus;
import models.Officer;
import models.OfficerRegistration;
import models.Project;

/**
 * <p>Interface for managing officer registrations for projects.</p>
 */
public interface IOfficerRegistrationService {
    /**
     * <p>Creates a registration for an officer to handle a project.</p>
     *
     * @param officer The officer making the registration.
     * @param project The project for the registration.
     * @return The created officer registration.
     */
    public OfficerRegistration createRegistration(Officer officer, Project project);

    /**
     * <p>Saves the officer registration.</p>
     *
     * @param officerRegistration The registration to save.
     */
    public void saveRegistration(OfficerRegistration officerRegistration);

    /**
     * <p>Gets the registration status of an officer.</p>
     *
     * @param officer The officer whose status is being checked.
     * @return The officer's registration status.
     */
    public RegistrationStatus getRegistrationStatus(Officer officer);
}

