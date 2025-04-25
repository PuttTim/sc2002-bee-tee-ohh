package interfaces;

import models.enums.RegistrationStatus;
import models.Officer;
import models.OfficerRegistration;
import models.Project;

public interface IOfficerRegistrationService {
    public OfficerRegistration createRegistration(Officer officer, Project project);
    public void saveRegistration(OfficerRegistration officerRegistration);
    public RegistrationStatus getRegistrationStatus(Officer officer);
}

