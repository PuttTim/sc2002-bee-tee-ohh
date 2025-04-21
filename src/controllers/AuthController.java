package controllers;

import exceptions.AuthenticationException;
import models.*;
import models.enums.Role;
import repositories.*;

import services.AuthService;
import services.ProjectService;

import utils.Hash;

import views.AuthView;
import views.CommonView;
import views.ProjectView;

public class AuthController {
    private static final String TEST_APPLICANT_NRIC = "S1234567A";
    private static final String TEST_OFFICER_NRIC = "S2345678B";
    private static final String TEST_MANAGER_NRIC = "S6543210I";
    private static final String TEST_PASSWORD = "password";

    public static void runAuthentication() {
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
                User user = AuthService.login(nric, password);
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

    private static void handleTestLogin() {
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
            User user = AuthService.login(nric, TEST_PASSWORD);
            AuthView.displayLoginSuccess(user.getName());
            dispatchToController(user);
        } catch (AuthenticationException e) {
            AuthView.displayLoginFailed("Test user not found. Please ensure test data is properly set up.");
        }
    }

    private static void dispatchToController(User user) {
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

        AuthService.logout();
        CommonView.displayMessage("Logged out of: " + user.getName());
    }

    private static void showApplicantMenu(Applicant user) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showApplicantMenu();
            try {
                switch (choice) {
                    case 1 -> ProjectController.viewAvailableProjects(user);
                    case 2 -> ApplicantController.newApplication(user);
                    case 3 -> ApplicantController.viewMyApplications(user);
                    case 4 -> EnquiryController.createNewEnquiry(user);
                    case 5 -> EnquiryController.viewApplicantEnquiries(user);
                    case 0 -> {return;}
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
        // System.out.println("Logging out of user: " + applicant.getName());
    }

    private static void showOfficerMenu(Officer officer) {
        boolean running = true;

        while (running) {
            int choice = AuthView.showOfficerMenu();
            try {
                switch (choice) {
                    case 1 -> OfficerController.registerToHandleProject(officer);
                    case 2 -> OfficerController.checkHandlerRegistration(officer);
                    case 3 -> OfficerController.viewHandledProjectDetails(officer);
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

    private static void showManagerMenu(Manager manager) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showManagerMenu();
            try {
                switch (choice) {
                    case 1 -> ManagerController.viewAllProjects(); 
                    case 2 -> ManagerController.viewHandledProjects(manager);
                    case 3 -> ManagerController.viewAllEnquiries();
                    case 4 -> ManagerController.createProject();
                    case 0 -> {return;}
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
    }

    private static void handleChangePassword(User user) {
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
            AuthService.changePassword(user, oldPassword, newPassword);
            AuthView.displayPasswordChangeSuccess();
        } catch (AuthenticationException e) {
            AuthView.displayPasswordChangeError(e.getMessage());
        }
    }
}