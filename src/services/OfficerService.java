package services;

import models.Officer;

public class OfficerService {
    public boolean officerHasExistingProject(Officer officer) {
        return officer.getHandledProject() != null;
    }
}
