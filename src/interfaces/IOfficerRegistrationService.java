package interfaces;

import models.Officer;
import models.OfficerRegistration;
import models.Project;
import models.enums.RegistrationStatus;

public interface IOfficerRegistrationService {
    public OfficerRegistration createRegistration(Officer officer, Project project);
    public void saveRegistration(OfficerRegistration officerRegistration);
    public RegistrationStatus getRegistrationStatus(Officer officer);
}

