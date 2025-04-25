package interfaces;

import models.Officer;

/**
 * Interface for managing officer-related operations.
 */
public interface IOfficerService {

    /**
     * Checks if the officer has an existing project.
     *
     * @param officer The officer to check.
     * @return true if the officer has an existing project, false otherwise.
     */
    boolean hasExistingProject(Officer officer);
}
