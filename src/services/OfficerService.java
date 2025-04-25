package services;

import java.util.List;

import interfaces.IOfficerService;
import models.Officer;
import models.Project;
import models.Registration;
import repositories.OfficerRepository;
import repositories.ProjectRepository;
import repositories.RegistrationRepository;
import models.enums.RegistrationStatus;

/**
 * Service class for managing officer-related operations.
 * <p>
 * This class provides methods for checking officer assignments and managing
 * officer registration status. It serves as the business logic layer between
 * controllers and the officer data repository.
 * </p>
 */
public class OfficerService implements IOfficerService {
    private static OfficerService instance;
    
    private OfficerService() {}
    
    public static OfficerService getInstance() {
        if (instance == null) {
            instance = new OfficerService();
        }
        return instance;
    }

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

    /**
     * Checks if an officer has any pending registration.
     *
     * @param officer the officer to check for pending registrations
     * @return {@code true} if the officer has at least one pending registration,
     *         {@code false} otherwise
     */
    @Override
    public boolean hasExistingRegistration(Officer officer) {
        List<Registration> registrations = RegistrationRepository.getByOfficer(officer);
        return registrations.stream()
            .anyMatch(r -> r.getRegistrationStatus() == RegistrationStatus.PENDING);
    }

    /**
     * Sets the registration status for an officer.
     *
     * @param officer the officer whose registration status is to be set
     */
    @Override
    public void setOfficerRegistration(Officer officer) {
        // Add implementation here if needed
    }

    /**
     * Retrieves a list of available projects.
     *
     * @return a list of projects that are visible
     */
    public List<Project> getAvailableProjects() {
        return ProjectRepository.getAll().stream()
            .filter(Project::isVisible)
            .toList();
    }

    /**
     * Checks if a project is eligible for registration by an officer.
     *
     * @param project the project to check eligibility for
     * @param officer the officer attempting to register
     * @return {@code true} if the project is eligible for registration,
     *         {@code false} otherwise
     */
    public boolean isProjectEligibleForRegistration(Project project, Officer officer) {
        if (project.getOfficerSlots() <= project.getOfficers().size()) {
            return false;
        }

        List<Registration> officerRegistrations = RegistrationRepository.getByOfficer(officer);
        
        if (officerRegistrations.stream()
            .anyMatch(r -> r.getProjectID().equals(project.getProjectID()))) {
            return false;
        }

        // Checks if application period conflicts with other projects
        List<Project> registeredProjects = ProjectRepository.getAll().stream()
            .filter(p -> officerRegistrations.stream()
                .anyMatch(r -> r.getProjectID().equals(p.getProjectID())))
            .toList();

        return registeredProjects.stream()
            .noneMatch(r -> r.getApplicationOpenDate().isBefore(project.getApplicationCloseDate()) &&
                r.getApplicationCloseDate().isAfter(project.getApplicationOpenDate()));
    }

    /**
     * Registers an officer for a project.
     *
     * @param officer the officer to register
     * @param project the project to register the officer for
     */
    public void registerOfficerForProject(Officer officer, Project project) {
        Registration registration = new Registration(officer, project.getProjectID());
        RegistrationRepository.add(registration);
    }

    /**
     * Retrieves the project assigned to an officer.
     *
     * @param officer the officer whose project is to be retrieved
     * @return the project assigned to the officer, or {@code null} if none exists
     */
    public Project getProjectByOfficer(Officer officer) {
        return ProjectRepository.getAll().stream()
            .filter(p -> p.getOfficers().contains(officer.getUserNRIC()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Retrieves a list of projects handled by an officer.
     *
     * @param officer the officer whose handled projects are to be retrieved
     * @return a list of projects handled by the officer
     */
    public List<Project> getHandledProjects(Officer officer) {
        return ProjectRepository.getAll().stream()
            .filter(p -> p.getOfficers().contains(officer.getUserNRIC()))
            .toList();
    }
}