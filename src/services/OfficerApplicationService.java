package services;

import models.Officer;
import models.OfficerApplication;
import models.Project;

/**
 * Service class that handles the creation and saving of officer applications for projects.
 */
public class OfficerApplicationService {

    /**
     * Creates a new officer application for a specific project.
     *
     * @param officer the officer applying for the project
     * @param project the project the officer is applying to
     * @return the created officer application
     */
    public static OfficerApplication createApplication(Officer officer, Project project) {
        OfficerApplication officerApplication = new OfficerApplication(officer.getUserNRIC(), project);
        saveOfficerApplication(officerApplication);
        return officerApplication;
    }

    /**
     * Saves the given officer application.
     *
     * placeholder method prints saved
     *
     * @param officerApplication the officer application to save
     */
    public static void saveOfficerApplication(OfficerApplication officerApplication) {
        System.out.println("saved");
    }
}
