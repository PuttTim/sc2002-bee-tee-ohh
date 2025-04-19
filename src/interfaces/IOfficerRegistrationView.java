package interfaces;

import models.enums.RegistrationStatus;

/**
 * Interface for views to display officer registration information.
 */
public interface IOfficerRegistrationView {

    /**
     * Displays a message that shows successful registration.
     */
    public void displayRegistrationSuccess();

    /**
     * Displays a message that shows registration failure, with a reason.
     *
     * @param reason the reason for the failed registration.
     */
    public void displayRegistrationFailure(String reason);

    /**
     * Displays the current registration status of an officer.
     *
     * @param registrationStatus the officer's registration status to be displayed.
     */
    public void displayRegistrationStatus(RegistrationStatus registrationStatus);
}
