package services;

import interfaces.IOfficerService;
import models.Officer;

public class OfficerService implements IOfficerService {
    public boolean hasExistingProject(Officer officer) {
        return officer.getHandledProject() != null;
    }
}
