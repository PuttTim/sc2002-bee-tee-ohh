package services;

import models.Officer;
import models.OfficerApplication;
import models.Project;

/**
 * Service class for managing officer project applications.
 * <p>
 * Provides functionality for creating and saving officer project applications.
 * Handles the association between officers and projects they are assigned to.
 * </p>
 */
public class OfficerApplicationService {

    /**
     * Creates a new officer application for a project.
     * <p>
     * Generates a new application record linking the officer to the specified project.
     * </p>
     *
     * @param officer the officer being assigned to the project
     * @param project the project the officer is being assigned to
     * @return the newly created {@code OfficerApplication} object
     */
    public static OfficerApplication createApplication(Officer officer, Project project) {
        OfficerApplication officerApplication = new OfficerApplication(officer.getUserNRIC(), project);
        saveOfficerApplication(officerApplication);
        return officerApplication;
    }

    /**
     * Saves an officer application record.
     * <p>
     * Currently outputs a confirmation message to the console.
     * In a production environment, this would persist the application to a database.
     * </p>
     *
     * @param officerApplication the application record to save
     */
    public static void saveOfficerApplication(OfficerApplication officerApplication) {
        System.out.println("saved");
    }
}