package interfaces;
import models.Officer;

/**
 * Interface for services managing officer information.
 */
public interface IOfficerService {

    /**
     * Checks if an officer has an existing project.
     *
     * @param officer the officer to be checked.
     * @return <code>true</code> if the officer has an existing project, <code>false</code> if not.
     */
    public boolean hasExistingProject(Officer officer);

    /**
     * Checks if an officer has an existing registration.
     *
     * @param officer the officer to be checked.
     * @return <code>true</code>> if the officer has an existing registration, <code>false</code> if not.
     */
    public boolean hasExistingRegistration(Officer officer);
    public void setOfficerRegistration(Officer officer);
}

