package services;

import interfaces.IOfficerService;
import models.Officer;

public class OfficerService implements IOfficerService {
    public boolean hasExistingProject(Officer officer) {
        return officer.getHandledProject() != null;
    }

    @Override
    public boolean hasExistingRegistration(Officer officer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasExistingRegistration'");
    }

    @Override
    public void setOfficerRegistration(Officer officer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOfficerRegistration'");
    }
}
