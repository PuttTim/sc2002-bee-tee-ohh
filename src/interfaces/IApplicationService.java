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
    List<Application> getProjectApplications(Project project);
    boolean approveApplication(Application application, Manager manager);
    boolean rejectApplication(Application application, Manager manager);
    boolean approveWithdrawal(Application application, Manager manager);
    boolean rejectWithdrawal(Application application, Manager manager);
    boolean bookApplication(Application application, Officer officer, String selectedUnitNumber);
}
