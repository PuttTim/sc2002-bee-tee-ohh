package interfaces;

import models.enums.RegistrationStatus;

/**
 * <p>Interface for displaying information related to officer registration.</p>
 * <ul>
 * <li>Handles the display of registration success and failure messages.</li>
 * <li>Shows the current registration status of an officer.</li>
 * </ul>
 */
public interface IOfficerRegistrationView {

    /**
     * Displays a message when the officer registration is successful.
     */
    public void displayRegistrationSuccess();

    /**
     * Displays a message when the officer registration fails, with the reason.
     *
     * @param reason The reason for the failure.
     */
    public void displayRegistrationFailure(String reason);

    /**
     * Displays the current registration status of the officer.
     *
     * @param registrationStatus The officer's registration status.
     */
    public void displayRegistrationStatus(RegistrationStatus registrationStatus);
}
