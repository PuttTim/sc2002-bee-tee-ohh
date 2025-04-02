package interfaces;

import enums.RegistrationStatus;
import models.Officer;
import models.OfficerRegistration;
import models.Project;

public interface IOfficerRegistrationService {
    public OfficerRegistration createRegistration(Officer officer, Project project);
    public void saveRegistration(OfficerRegistration officerRegistration);
    public RegistrationStatus checkRegistrationStatus(Officer officer);
}

