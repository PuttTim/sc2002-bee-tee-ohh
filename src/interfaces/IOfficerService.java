package interfaces;

import models.Officer;

/**
 * <p>Interface for managing officer-related operations, such as checking existing projects and registrations.</p>
 * <ul>
 * <li>Checks if an officer has an existing project.</li>
 * <li>Checks if an officer has an existing registration.</li>
 * <li>Sets the registration for an officer.</li>
 * </ul>
 */
public interface IOfficerService {

    /**
     * Checks if the officer has an existing project.
     *
     * @param officer The officer to check.
     * @return true if the officer has an existing project, false otherwise.
     */
    public boolean hasExistingProject(Officer officer);

    /**
     * Checks if the officer has an existing registration.
     *
     * @param officer The officer to check.
     * @return true if the officer has an existing registration, false otherwise.
     */
    public boolean hasExistingRegistration(Officer officer);

    /**
     * Sets the registration for the officer.
     *
     * @param officer The officer to set the registration for.
     */
    public void setOfficerRegistration(Officer officer);
}
