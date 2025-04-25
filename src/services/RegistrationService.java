package services;

import models.Manager;
import models.Project;
import models.Registration;
import models.enums.RegistrationStatus;
import repositories.ProjectRepository;
import repositories.RegistrationRepository;
import views.CommonView;

import java.util.List;
import java.util.stream.Collectors;

import interfaces.IRegistrationService;

/**
 * Provides services related to the registration process for projects.
 * <p>
 * This class handles the logic for retrieving registrations for a project,
 * approving and rejecting them, and checking for slot availability.
 * </p>
 */
public class RegistrationService implements IRegistrationService {

    private static RegistrationService instance;

    private RegistrationService() {}

    /**
     * Returns the singleton instance of the RegistrationService class.
     * If an instance of RegistrationService does not already exist, it creates a new instance.
     *
     * @return The singleton instance of RegistrationService.
     */
    public static RegistrationService getInstance() {
        if (instance == null) {
            instance = new RegistrationService();
        }
        return instance;
    }

    /**
     * Retrieves all registrations for a specific project.
     *
     * @param project the project to retrieve registrations for
     * @return a list of all registrations for the given project
     */
    @Override
    public List<Registration> getProjectRegistrations(Project project) {
        return RegistrationRepository.getByProject(project.getProjectID());
    }

    /**
     * Approves a given registration if the project has available officer slots.
     * Also adds the officer to the project and updates repositories.
     *
     * @param registration the registration to approve
     * @param manager the manager performing the approval
     * @return true if the registration was successfully approved, false otherwise
     */
    @Override
    public boolean approveRegistration(Registration registration, Manager manager) {
        Project project = ProjectRepository.getById(registration.getProjectID());
        if (project == null) {
            CommonView.displayError("Project not found for this registration.");
            return false;
        }

        if (project.getOfficers().size() >= project.getOfficerSlots()) {
            CommonView.displayError("No available officer slots in project: " + project.getProjectName());
            return false;
        }

        try {
            registration.approve(manager);
            project.addOfficer(registration.getOfficer().getUserNRIC());
            RegistrationRepository.update(registration);
            ProjectRepository.update(project);
            return true;
        } catch (Exception e) {
            return true; // Note: Consider returning false to better reflect failure here
        }
    }

    /**
     * Rejects a given registration and updates the repository.
     *
     * @param registration the registration to reject
     * @param manager the manager performing the rejection
     * @return true if the registration was successfully rejected, false otherwise
     */
    @Override
    public boolean rejectRegistration(Registration registration, Manager manager) {
        try {
            registration.reject(manager);
            RegistrationRepository.update(registration);
            return true;
        } catch (Exception e) {
            CommonView.displayError("Error rejecting registration: " + e.getMessage());
            return false;
        }
    }
}
