package services;

import java.util.List;

import interfaces.IOfficerService;
import models.Officer;
import models.Registration;
import repositories.OfficerRepository;
import repositories.RegistrationRepository;

/**
 * Service class for managing officer-related operations.
 * <p>
 * This class provides methods for checking officer assignments and managing
 * officer registration status. It serves as the business logic layer between
 * controllers and the officer data repository.
 * </p>
 */
public class OfficerService implements IOfficerService {

    /**
     * Checks if an officer is currently assigned to any project.
     *
     * @param officer the officer to check for project assignments
     * @return {@code true} if the officer has at least one project assignment,
     *         {@code false} otherwise
     */
    @Override
    public boolean hasExistingProject(Officer officer) {
        return OfficerRepository.hasExistingProject(officer);
    }
}