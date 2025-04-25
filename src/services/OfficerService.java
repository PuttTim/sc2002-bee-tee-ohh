package services;

import models.Officer;
import repositories.OfficerRepository;

/**
 * Service class for managing officer-related operations.
 * <p>
 * This class provides methods for checking officer assignments and managing
 * officer registration status. It serves as the business logic layer between
 * controllers and the officer data repository.
 * </p>
 */
public class OfficerService {

    /**
     * Checks if an officer is currently assigned to any project.
     *
     * @param officer the officer to check for project assignments
     * @return {@code true} if the officer has at least one project assignment,
     *         {@code false} otherwise
     */
    public static boolean hasExistingProject(Officer officer) {
        return OfficerRepository.hasExistingProject(officer);
    }

    /**
     * Checks if an officer has an existing registration.
     *
     * @param officer the officer to check registration status for
     * @return {@code true} if the officer has existing registration,
     *         {@code false} otherwise
     */
    public static boolean hasExistingRegistration(Officer officer) {
        // TODO: Implement this
        return false;
    }

    /**
     * Completes the registration process for an officer.
     *
     * @param officer the officer to mark as registered
     */
    public static void setOfficerRegistration(Officer officer) {
        // TODO: Implement this
    }
}