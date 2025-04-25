package interfaces;

import java.util.List;

import models.Application;
import models.Manager;
import models.Officer;
import models.Project;

/**
 * Interface for managing housing application operations.
 */
public interface IApplicationService {
    /**
     * Retrieves all applications submitted for a specific project.
     *
     * @param project The project to retrieve applications for.
     * @return A list of applications associated with the project.
     */
    List<Application> getProjectApplications(Project project);

    /**
     * Approves a pending application.
     *
     * @param application The application to approve.
     * @param manager     The manager performing the approval.
     * @return True if the application was approved successfully, false otherwise.
     */
    boolean approveApplication(Application application, Manager manager);

    /**
     * Rejects a pending application.
     *
     * @param application The application to reject.
     * @param manager     The manager performing the rejection.
     * @return True if the application was rejected successfully, false otherwise.
     */
    boolean rejectApplication(Application application, Manager manager);

    /**
     * Approves a withdrawal request for an application.
     *
     * @param application The application to withdraw.
     * @param manager     The manager approving the withdrawal.
     * @return True if the withdrawal was approved successfully, false otherwise.
     */
    boolean approveWithdrawal(Application application, Manager manager);

    /**
     * Rejects a withdrawal request for an application.
     *
     * @param application The application to continue processing.
     * @param manager     The manager rejecting the withdrawal.
     * @return True if the withdrawal was rejected successfully, false otherwise.
     */
    boolean rejectWithdrawal(Application application, Manager manager);

    /**
     * Books a unit for a successful application.
     *
     * @param application       The application to book a unit for.
     * @param officer           The officer handling the booking.
     * @param selectedUnitNumber The specific unit number to assign.
     * @return True if booking was successful, false otherwise.
     */
    boolean bookApplication(Application application, Officer officer, String selectedUnitNumber);

}
