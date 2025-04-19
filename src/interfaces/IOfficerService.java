package interfaces;

import models.Officer;

public interface IOfficerService {
    public boolean hasExistingProject(Officer officer);
    public boolean hasExistingRegistration(Officer officer);
    public void setOfficerRegistration(Officer officer);
}

