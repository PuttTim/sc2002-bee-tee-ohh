package controllers;

import exceptions.AuthenticationException;
import models.*;
import services.AuthService;
import views.AuthView;
import views.CommonView;
import repositories.ApplicantRepository;
import repositories.ManagerRepository;
import repositories.OfficerRepository;
import repositories.ProjectRepository;
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
            case APPLICANT:
                Applicant applicant = ApplicantRepository.getByNRIC(user.getUserNRIC());
                showApplicantMenu(applicant);
                break;
            case OFFICER:
                Officer officer = OfficerRepository.getByNRIC(user.getUserNRIC());
                showOfficerMenu(officer);
                break;
            case MANAGER:
                Manager manager = ManagerRepository.getByNRIC(user.getUserNRIC());
                showManagerMenu(manager);
                break;
            default:
                throw new IllegalStateException("Unknown role: " + user.getRole());
        }
    }

    private static void showApplicantMenu(Applicant applicant) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showApplicantMenu();
            try {
                switch (choice) {
                    case 1:
                        ApplicantController.viewAvailableProjects();
                        break;
                    case 2:
                        ApplicantController.submitApplication(applicant);
                        break;
                    case 3:
                        ApplicantController.viewMyApplications(applicant);
                        break;
                    case 4:
                        ApplicantController.submitEnquiry(applicant);
                        break;
                    case 5:
                        ApplicantController.viewMyEnquiries(applicant);
                        break;
                    case 6:
                        handleChangePassword(applicant);
                        break;
                    case 7:
                        running = false;
                        break;
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
    }

    private static void showOfficerMenu(Officer officer) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showOfficerMenu();
            try {
                switch (choice) {
                    case 1 -> {
                        List<Project> projects = ProjectRepository.getAll();
                        if (projects.isEmpty()) {
                            CommonView.displayMessage("No projects available for registration.");
                            break;
                        }
                        CommonView.displayHeader("Available Projects");
                        for (int i = 0; i < projects.size(); i++) {
                            CommonView.displayMessage((i + 1) + ". " + projects.get(i).getProjectName());
                        }
                        int projectChoice = CommonView.promptInt("Select project number (or 0 to cancel): ", 0, projects.size());
                        if (projectChoice > 0) {
                            Project selectedProject = projects.get(projectChoice - 1);
                            OfficerController.registerToHandleProject(officer, selectedProject);
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
    }

    private static void showManagerMenu(Manager manager) {
        boolean running = true;
        while (running) {
            int choice = AuthView.showManagerMenu();
            try {
                switch (choice) {
                    case 1 -> ManagerController.createProject();
                    case 2 -> {
                        String projectName = AuthView.getProjectName();
                        String location = AuthView.getNewLocation();
                        String startDate = AuthView.getApplicationDate("start");
                        String endDate = AuthView.getApplicationDate("end");
                        ManagerController.editProject(projectName, location, startDate, endDate);
                    }
                    case 3 -> {
                        String projectName = AuthView.getProjectName();
                        ManagerController.deleteProject(projectName);
                    }
                    case 4 -> ManagerController.toggleVisibility();
                    case 5 -> ManagerController.viewAllProjects();
                    case 6 -> {
                        String projectName = AuthView.getProjectName();
                        Project project = ProjectRepository.getByName(projectName);
                        if (project != null) {
                            ManagerController.viewProjectEnquiries(project);
                        } else {
                            AuthView.displayProjectNotFound();
                        }
                    }
                    case 7 -> handleOfficerRegistrations();
                    case 8 -> handleChangePassword(manager);
                    case 9 -> running = false;
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
    }

    private static void handleChangePassword(User user) {
        AuthView.showChangePasswordHeader();
        String oldPassword = AuthView.getPassword();
        String newPassword = AuthView.getPassword();
        
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
                String projectName = AuthView.getProjectName();
                Project project = ProjectRepository.getByName(projectName);
                if (project != null) {
                    ManagerController.approveOfficerRegistration(project, choice == 2);
                } else {
                    AuthView.displayProjectNotFound();
                }
            }
        }
    }
}