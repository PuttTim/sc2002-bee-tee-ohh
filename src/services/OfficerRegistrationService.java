package services;

import enums.RegistrationStatus;
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

    @Override
    public OfficerRegistration createRegistration(Officer officer, Project project) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createRegistration'");
    }

    @Override
    public void saveRegistration(OfficerRegistration officerRegistration) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveRegistration'");
    }

    @Override
    public RegistrationStatus checkRegistrationStatus(Officer officer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkRegistrationStatus'");
    }
}