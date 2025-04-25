package interfaces;

import java.util.List;

import models.Manager;
import models.Project;
import models.Registration;

/**
 * Interface for services related to the registration process for projects.
 */
public interface IRegistrationService {
    /**
     * Gets the list of project registrations.
     *
     * @param project the projects that can be registered for
     * @return a list of project registrations
     */
    List<Registration> getProjectRegistrations(Project project);

    /**
     * Approves a registration.
     *
     * @param registration the registration to approve
     * @param manager the manager to approve the registration
     * @return <code>true</code> if the registration can be approved,
     * <code>false</code> if not.
     */
    boolean approveRegistration(Registration registration, Manager manager);

    /**
     * Rejects a registration.
     *
     * @param registration the registration to reject
     * @param manager the manager to reject the registration
     * @return <code>true</code> if the registration can be rejected,
     * <code>false</code> if not.
     */
    boolean rejectRegistration(Registration registration, Manager manager);
}
