package services;

import interfaces.IOfficerRegistrationService;
import models.Officer;
import models.OfficerRegistration;
import models.Project;

public class OfficerRegistrationService implements IOfficerRegistrationService {
    public OfficerRegistration createApplication(Officer officer, Project project) {
        OfficerRegistration officerRegistration = new OfficerRegistration(officer.getNric(), project);
        saveOfficerApplication(officerRegistration);
        return officerRegistration;
    }

    public void saveOfficerApplication(OfficerRegistration officerRegistration) {
        System.out.println("saved");
    }
}