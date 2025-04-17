package controllers;

import exceptions.AuthenticationException;
import models.*;
import services.AuthService;
import utils.Hash;
import views.AuthView;
import views.CommonView;
import views.FilterView;
import views.ProjectView;
import repositories.*;
import services.ProjectService;
import java.util.List;

public class AuthController {
    private static final String TEST_APPLICANT_NRIC = "S1234567A";
    private static final String TEST_OFFICER_NRIC = "S2345678B";
    private static final String TEST_MANAGER_NRIC = "S6789012F";
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
        switch (user.getRole()) {
            case APPLICANT -> showApplicantMenu(ApplicantRepository.getByNRIC(user.getUserNRIC()));
            case OFFICER -> showOfficerMenu(OfficerRepository.getByNRIC(user.getUserNRIC()));
            case MANAGER -> showManagerMenu(ManagerRepository.getByNRIC(user.getUserNRIC()));
            default -> throw new IllegalStateException("Unknown role: " + user.getRole());
        }
    }

    private static void showApplicantMenu(Applicant applicant) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showApplicantMenu();
            try {
                switch (choice) {
                    case 1 -> ProjectController.viewAvailableProjects();
                    case 2 -> ApplicantController.newApplication(applicant);
                    case 3 -> ApplicantController.viewMyApplications(applicant);
                    case 4 -> EnquiryController.createNewEnquiry(applicant);
                    case 5 -> EnquiryController.viewApplicantEnquiries(applicant);
                    case 6 -> handleChangePassword(applicant);
                    case 7 -> running = false;
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
        System.out.println("Logging out of user: " + applicant.getName());
        AuthService.logout();
    }

    private static void showOfficerMenu(Officer officer) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showOfficerMenu();
            try {
                switch (choice) {
                    case 1 -> {
//                        List<Filter> filters = FilterView.getFilters();
//                        List<Project> projects = ProjectService.getProjects(filters);
                        List<Project> projects = ProjectService.getVisibleProjects();
                        if (projects.isEmpty()) {
                            CommonView.displayMessage("No projects available for registration.");
                            break;
                        }
                        ProjectView.displayProjectList(projects);
                        int projectChoice = CommonView.promptInt("Select project number (or 0 to cancel): ", 0, projects.size());
                        if (projectChoice > 0) {
                            OfficerController.registerToHandleProject(officer, projects.get(projectChoice - 1));
                        }
                    }
                    case 2 -> OfficerController.checkHandlerRegistration(officer);
                    case 3 -> OfficerController.viewHandledProjectDetails(officer);
                    case 4 -> OfficerController.manageProjectEnquiries(officer);
                    case 5 -> OfficerController.processApplication();
                    case 6 -> OfficerController.generateReceipt();
                    case 7 -> handleChangePassword(officer);
                    case 8 -> running = false;
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
        System.out.println("Logging out of user: " + officer.getName());
        AuthService.logout();
    }

    private static void showManagerMenu(Manager manager) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showManagerMenu();
            try {
                switch (choice) {
                    case 1 -> ProjectController.createProject();
                    case 2 -> ProjectController.editProject();
                    case 3 -> ProjectController.deleteProject();
                    case 4 -> ProjectController.toggleProjectVisibility();
                    case 5 -> {
                        ProjectController.viewAllProjects();
                    }
                    case 6 -> {
                        String projectName = ProjectView.getProjectName();
                        ProjectController.viewProjectEnquiries(projectName);
                    }
                    case 7 -> handleOfficerRegistrations();
                    case 8 -> handleChangePassword(manager);
                    case 9 -> running = false;
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
        System.out.println("Logging out of user: " + manager.getName());
        AuthService.logout();
    }

    private static void handleChangePassword(User user) {
        AuthView.showChangePasswordHeader();
        String oldPassword = CommonView.prompt("Enter old password: ");
        if (oldPassword.isEmpty()) {
            AuthView.displayPasswordChangeError("Old password cannot be empty.");
            return;
        }
        if (!Hash.verifyPassword(oldPassword, user.getPassword())) {
            AuthView.displayPasswordChangeError("Old password is incorrect.");
            return;
        }

        String newPassword = CommonView.prompt("Enter new password: ");
        if (newPassword.isEmpty()) {
            AuthView.displayPasswordChangeError("New password cannot be empty.");
            return;
        }
        if (Hash.verifyPassword(newPassword, user.getPassword())) {
            AuthView.displayPasswordChangeError("New password cannot be the same as old password.");
            return;
        }
        
        String confirmPassword = CommonView.prompt("Confirm new password: ");
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

    private static void handleOfficerRegistrations() {
        int choice = AuthView.showOfficerRegistrationMenu();
        switch (choice) {
            case 1 -> ManagerController.viewOfficerRegistrations();
            case 2, 3 -> {
                String projectName = ProjectView.getProjectName();
                Project project = ProjectService.getProjectByName(projectName);
                if (project != null) {
                    ManagerController.approveOfficerRegistration(project, choice == 2);
                } else {
                    ProjectView.displayProjectNotFound();
                }
            }
        }
    }
}