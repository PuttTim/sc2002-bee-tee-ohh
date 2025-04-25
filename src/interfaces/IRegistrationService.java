package interfaces;

import java.util.List;

import models.Manager;
import models.Project;
import models.Registration;

/**
 * Interface for services related to the registration process for projects.
 */
public interface IRegistrationService {
    List<Registration> getProjectRegistrations(Project project);
    boolean approveRegistration(Registration registration, Manager manager);
    boolean rejectRegistration(Registration registration, Manager manager);
}
