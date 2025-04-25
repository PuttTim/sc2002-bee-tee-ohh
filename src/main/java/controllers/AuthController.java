package controllers;

import exceptions.AuthenticationException;
import models.*;
import models.enums.Role;
import repositories.*;

import services.AuthService;

import utils.Hash;

import views.AuthView;
import views.CommonView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Controller class to handle authentication processes.
 * <p>Authentication processes include:</p>
 * <ul>
 *     <li>User login</li>
 *     <li>Navigation based on roles</li>
 *     <li>Password management</li>
 * </ul>
 */
public class AuthController {
    private static final String TEST_APPLICANT_NRIC = "S1234567A";
    private static final String TEST_OFFICER_NRIC = "S2345678B";
    private static final String TEST_MANAGER_NRIC = "S6543210I";
    private static final String TEST_PASSWORD = "password";

    private final AuthService authService;
    private final ApplicantController applicantController;
    private final OfficerController officerController;
    private final ManagerController managerController;
    private final ProjectController projectController;
    private final EnquiryController enquiryController;
    
    public AuthController() {
        this.authService = AuthService.getInstance();
        this.applicantController = new ApplicantController();
        this.officerController = new OfficerController();
        this.managerController = new ManagerController();
        this.projectController = new ProjectController();
        this.enquiryController = new EnquiryController();
    }

    /**
     * Controller class to handle authentication processes.
     * <p>Authentication processes include:</p>
     * <ul>
     *     <li>User login</li>
     *     <li>Navigation based on roles</li>
     *     <li>Password management</li>
     * </ul>
     */
    public void runAuthentication() {
        while (true) {
            AuthView.displayLoginHeader();
            boolean isTestingMode = AuthView.showTestingMenu();

            if (isTestingMode) {
                handleTestLogin();
                continue;
            }
            
            String nric = AuthView.getNRIC();
            if (nric.equalsIgnoreCase("exit")) {
                return;
            }
            
            String password = AuthView.getPassword();
            
            try {
                User user = authService.login(nric, password);
                AuthView.displayLoginSuccess(user.getName());
                dispatchToController(user);
            } catch (AuthenticationException e) {
                AuthView.displayLoginFailed(e.getMessage());
                if (!AuthView.promptRetryLogin()) {
                    return;
                }
            }
        }
    }

    /**
     * Handles login for predefined users, for testing purposes.
     */
    private void handleTestLogin() {
        int choice = AuthView.showTestUserOptions();
        String nric;
        
        switch (choice) {
            case 1 -> nric = TEST_APPLICANT_NRIC;
            case 2 -> nric = TEST_OFFICER_NRIC;
            case 3 -> nric = TEST_MANAGER_NRIC;
            default -> {
                CommonView.displayError("Invalid choice!");
                return;
            }
        }

        try {
            User user = authService.login(nric, TEST_PASSWORD);
            AuthView.displayLoginSuccess(user.getName());
            dispatchToController(user);
        } catch (AuthenticationException e) {
            AuthView.displayLoginFailed("Test user not found. Please ensure test data is properly set up.");
        }
    }

    /**
     * Dispatches the user (logged-in) to the appropriate menu, based on their role.
     *
     * @param user the user who is shown different menus based on their role (applicant, officer or manager).
     */
    private void dispatchToController(User user) {
        boolean running = true;
        switch (user.getRole()) {
            case APPLICANT -> {
                while (running) {
                    switch (AuthView.showApplicantMainMenu()) {
                        case 1 -> showApplicantMenu(ApplicantRepository.getByNRIC(user.getUserNRIC()));
                        case 2 -> handleChangePassword(user);
                        case 3 -> { running = false; }
                        default -> CommonView.displayError("Invalid option. Please try again.");
                    }
                }
            }
            case OFFICER -> {
                while (running) {

                    switch (AuthView.showOfficerMainMenu()) {
                        case 1 -> {
                            UserRepository.setUserMode(Role.APPLICANT);
                            showApplicantMenu(OfficerRepository.getByNRIC(user.getUserNRIC()));
                        }
                        case 2 -> {
                            UserRepository.setUserMode(Role.OFFICER);
                            showOfficerMenu(OfficerRepository.getByNRIC(user.getUserNRIC()));
                        }
                        case 3 -> handleChangePassword(user);
                        case 4 -> { running = false; }
                        default -> CommonView.displayError("Invalid option. Please try again.");
                    }
                }
            }
            case MANAGER -> {
                while (running) {
                    switch (AuthView.showManagerMainMenu()) {
                        case 1 -> showManagerMenu(ManagerRepository.getByNRIC(user.getUserNRIC()));
                        case 2 -> handleChangePassword(user);
                        case 3 -> { running = false; }
                        default -> CommonView.displayError("Invalid option. Please try again.");
                    }
                }
            }
            default -> throw new IllegalStateException("Unknown role: " + user.getRole());
        }

        authService.logout();
        CommonView.displayMessage("Logged out of: " + user.getName());
    }

    /**
     * Displays the menu for applicants and handles applicant choices.
     *
     * @param user an applicant for a project.
     */
    private void showApplicantMenu(Applicant user) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showApplicantMenu();
            try {
                switch (choice) {
                    case 1 -> applicantController.viewAvailableProjects(user);
                    case 2 -> applicantController.manageApplications(user);
                    case 3 -> enquiryController.viewApplicantEnquiries(user);
                    case 4 -> enquiryController.createNewEnquiry(user);
                    case 0 -> {return;}
                    default -> CommonView.displayError("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                CommonView.displayError("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the menu for officers and handles officer choices.
     *
     * @param officer an officer for projects.
     */
    private void showOfficerMenu(Officer officer) {
        boolean running = true;

        while (running) {
            int choice = AuthView.showOfficerMenu();
            try {
                switch (choice) {
                    case 1 -> officerController.registerToHandleProject(officer);
                    case 2 -> officerController.checkHandlerRegistration(officer);
                    case 3 -> officerController.viewHandledProjectDetails(officer);
                    case 0 -> {return;}
                    default -> CommonView.displayError("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            } catch (Exception e) {
                CommonView.displayError("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays the menu for managers and handles manager choices.
     *
     * @param manager the manager for projects.
     */
    private void showManagerMenu(Manager manager) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showManagerMenu();
            try {
                switch (choice) {
                    case 1 -> managerController.viewAllProjects(); 
                    case 2 -> managerController.viewHandledProjects(manager);
                    case 3 -> managerController.viewAllEnquiries(manager);
                    case 4 -> managerController.createProject(manager);
                    case 0 -> {return;}
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
    }

    /**
     * Handles password changes for users.
     * Allows users to change their passwords. Users have to verify their old password, and enter a new one.
     *
     * @param user that wants to change their password.
     */
    private void handleChangePassword(User user) {
        AuthView.showChangePasswordHeader();
        String oldPassword = CommonView.prompt("Enter old password: ");
        if (oldPassword.equals("0")) {
            CommonView.displayMessage("Password change cancelled.");
            return;
        }

        if (oldPassword.isEmpty()) {
            AuthView.displayPasswordChangeError("Old password cannot be empty.");
            return;
        }
        if (!Hash.verifyPassword(oldPassword, user.getPassword())) {
            AuthView.displayPasswordChangeError("Old password is incorrect.");
            return;
        }

        String newPassword = CommonView.prompt("Enter new password: ");
        if (newPassword.equals("0")) {
            CommonView.displayMessage("Password change cancelled.");
            return;
        }
        if (newPassword.isEmpty()) {
            AuthView.displayPasswordChangeError("New password cannot be empty.");
            return;
        }
        if (Hash.verifyPassword(newPassword, user.getPassword())) {
            AuthView.displayPasswordChangeError("New password cannot be the same as old password.");
            return;
        }
        
        String confirmPassword = CommonView.prompt("Confirm new password: ");
        if (confirmPassword.equals("0")) {
            CommonView.displayMessage("Password change cancelled.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AuthView.displayPasswordChangeError("New passwords do not match.");
            return;
        }

        try {
            authService.changePassword(user, oldPassword, newPassword);
            AuthView.displayPasswordChangeSuccess();
        } catch (AuthenticationException e) {
            AuthView.displayPasswordChangeError(e.getMessage());
        }
    }
}