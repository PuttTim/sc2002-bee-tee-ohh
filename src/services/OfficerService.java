package services;

import interfaces.IOfficerService;

import models.Officer;
import repositories.OfficerRepository;

/**
 * Service class provides utility methods for officer-related actions and checks.
 */
public class OfficerService {

    /**
     * Checks if the given officer is already assigned to a project.
     *
     * @param officer the officer to check
     * @return <code>true</code> if the officer has an existing project,
     * <code>false</code> if the officer has no existing projects
     */
    public static boolean hasExistingProject(Officer officer) {
        return OfficerRepository.hasExistingProject(officer);
    }

    /**
     * Checks if the officer has already submitted a registration.
     *
     * @param officer the officer to check
     * @return <code>true</code> if the officer has a registration
     */
    public static boolean hasExistingRegistration(Officer officer) {
        // TODO: Implement this
        return false;
    }

    /**
     * Creates a registration for the given officer.
     *
     * @param officer the officer to register
     */
    public static void setOfficerRegistration(Officer officer) {
        // TODO: Implement this
    }
}
