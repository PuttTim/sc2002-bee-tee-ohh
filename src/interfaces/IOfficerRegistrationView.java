package interfaces;

import models.enums.RegistrationStatus;

public interface IOfficerRegistrationView {
    public void displayRegistrationSuccess();
    public void displayRegistrationFailure(String reason);
    public void displayRegistrationStatus(RegistrationStatus registrationStatus);
}
