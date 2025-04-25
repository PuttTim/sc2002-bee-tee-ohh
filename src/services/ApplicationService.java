package services;

import java.util.List;
import java.util.stream.Collectors;

import interfaces.IApplicationService;
import models.Application;
import models.Manager;
import models.Officer;
import models.Project;
import models.Receipt;
import models.enums.ApplicationStatus;
import repositories.ApplicationRepository;
import repositories.ProjectRepository;
import repositories.ReceiptRepository;
import views.CommonView;

/**
 * Service class for managing housing application operations.
 * <p>
 * Provides functionality for retrieving, approving, rejecting, and booking housing applications.
 * Interacts with repositories to persist changes and enforce business rules.
 * </p>
 */
public class ApplicationService implements IApplicationService {
    private static ApplicationService instance;
    
    private ApplicationService() {}

    /**
     * Returns the singleton instance of the ApplicationService class.
     * If an instance of ApplicationService does not already exist, it creates a new instance.
     *
     * @return The singleton instance of ApplicationService.
     */
    public static ApplicationService getInstance() {
        if (instance == null) {
            instance = new ApplicationService();
        }
        return instance;
    }

    /**
     * Retrieves all applications for a specific project.
     *
     * @param project The project to filter applications by
     * @return List of {@code Application} objects for the given project
     */
    @Override
    public List<Application> getProjectApplications(Project project) {
        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getProjectId().equals(project.getProjectID()))
                .collect(Collectors.toList());
    }

    /**
     * Approves an application if it meets approval criteria.
     * The officer's NRIC is recorded
     *
     * @param application The application to approve
     * @param manager The manager approving the application
     * @return {@code true} if approval was successful, {@code false} otherwise
     */
    @Override
    public boolean approveApplication(Application application, Manager manager) {
        if (application == null || !application.canApprove()) {
            return false;
        }
        Project project = ProjectRepository.getById(application.getProjectId());
        if (project == null || project.getAvailableUnits(application.getSelectedFlatType()) <= 0) {
            CommonView.displayError("No available units left for this flat type.");
            return false;
        }

        try {
            application.approve(manager.getUserNRIC());
            ApplicationRepository.saveAll();
            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error approving application: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during approval: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rejects an application if it meets rejection criteria.
     * The officer's NRIC is recorded.
     *
     * @param application The application to reject
     * @param manager The manager rejecting the application
     * @return {@code true} if rejection was successful, {@code false} otherwise
     */
    @Override
    public boolean rejectApplication(Application application, Manager manager) {
        if (application == null || !application.canReject()) {
            return false;
        }
        try {
            application.reject(manager.getUserNRIC());
            ApplicationRepository.saveAll();
            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error rejecting application: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during rejection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Approves the withdrawal request for a given application.
     *
     * This method checks if the withdrawal can be approved based on the current state of the application.
     * If approval is possible, it updates the application's status to reflect the approval and saves the updated
     * state to the repository.
     *
     * @param application The application for which the withdrawal request is to be approved.
     * @param manager The manager performing the approval action.
     * @return boolean Returns true if the withdrawal was successfully approved, false otherwise.
     * @throws IllegalStateException if the application cannot approve the withdrawal due to its current state.
     */

    @Override
    public boolean approveWithdrawal(Application application, Manager manager) {
        if (application == null || !application.canApproveWithdrawal()) {
            return false;
        }
        try {
            application.approveWithdrawal(manager.getUserNRIC());
            ApplicationRepository.saveAll();
            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error approving withdrawal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during withdrawal approval: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rejects the withdrawal request for a given application.
     *
     * This method checks if the withdrawal can be rejected based on the current state of the application.
     * If rejection is possible, it updates the application's status to reflect the rejection and saves the updated
     * state to the repository.
     *
     * @param application The application for which the withdrawal request is to be rejected.
     * @param manager The manager performing the rejection action.
     * @return boolean Returns true if the withdrawal was successfully rejected, false otherwise.
     * @throws IllegalStateException if the application cannot reject the withdrawal due to its current state.
     */

    @Override
    public boolean rejectWithdrawal(Application application, Manager manager) {
        if (application == null || !application.canRejectWithdrawal()) {
            return false;
        }
        try {
            application.rejectWithdrawal(manager.getUserNRIC());
            ApplicationRepository.saveAll();
            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error rejecting withdrawal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during withdrawal rejection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Books a unit for an approved application.
     * <p>
     * Performs several validations before booking:
     * <ul>
     *   <li>Checks if the application is in a bookable state</li>
     *   <li>Verifies the unit number isn't already taken</li>
     *   <li>Ensures the project has available units</li>
     * </ul>
     *
     * @param application The application to book
     * @param officer The officer processing the booking
     * @param selectedUnitNumber The unit number being assigned
     * @return {@code true} if booking was successful, {@code false} otherwise
     */
    @Override
    public boolean bookApplication(Application application, Officer officer, String selectedUnitNumber) {
        if (application == null || !application.canBook()) {
            return false;
        }

        Project project = ProjectRepository.getById(application.getProjectId());
        List<Receipt> receipts = ReceiptRepository.getByProjectID(project.getProjectID());
        if (receipts.stream().anyMatch(r -> r.getUnitNumber().equals(selectedUnitNumber))) {
            CommonView.displayError("Unit number already booked. Please select a different unit.");
            return false;
        }

        if (project.getAvailableUnits(application.getSelectedFlatType()) <= 0) {
            return false;
        }

        if (selectedUnitNumber == null || selectedUnitNumber.trim().isEmpty()) { 
             System.err.println("Error booking application: Unit number cannot be empty.");
             return false;
        }

        try {
            project.reduceFlatCount(application.getSelectedFlatType());
            application.book();

            ProjectRepository.saveAll();
            ApplicationRepository.saveAll();

            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error booking application: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
