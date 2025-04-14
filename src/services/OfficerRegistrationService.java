package services;

import interfaces.IOfficerRegistrationService;
import models.Officer;
import models.OfficerRegistration;
import models.Project;
import models.enums.RegistrationStatus;

public class OfficerRegistrationService implements IOfficerRegistrationService {
    public OfficerRegistration createApplication(Officer officer, Project project) {
        OfficerRegistration officerRegistration = new OfficerRegistration(officer.getUserNRIC(), project);
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
    public RegistrationStatus getRegistrationStatus(Officer officer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkRegistrationStatus'");
    }
}