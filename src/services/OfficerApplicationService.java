package services;

import models.Officer;
import models.OfficerApplication;
import models.Project;

public class OfficerApplicationService {
    public OfficerApplication createApplication(Officer officer, Project project) {
        OfficerApplication officerApplication = new OfficerApplication(officer.getNric(), project);
        saveOfficerApplication(officerApplication);
        return officerApplication;
    }

    public void saveOfficerApplication(OfficerApplication officerApplication) {
        System.out.println("saved");
    }
}