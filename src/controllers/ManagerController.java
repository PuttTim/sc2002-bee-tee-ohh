package controllers;

import java.util.List;
import java.util.stream.Collectors;

import models.Manager;
import models.Project;
import models.Registration;
import models.enums.RegistrationStatus;
import repositories.OfficerRepository;
import services.ProjectService;
import services.RegistrationService;
import views.CommonView;
import views.ManagerView;
import views.ProjectView;

/**
 * Controller for handling all actions available to a Manager user.
 * <p>
 * This includes functionality for:
 * <ul>
 *   <li>Viewing and managing projects assigned to the manager</li>
 *   <li>Managing officer registrations for projects</li>
 *   <li>Accessing project-related enquiries</li>
 * </ul>
 */
public class ManagerController {

    /**
     * Displays all projects handled by a given manager and lets them manage selected projects.
     * <p>
     * For each selected project, the manager can:
     * <ul>
     *   <li>Manage officer registrations</li>
     *   <li>View and manage enquiries</li>
     *   <li>Access other project management features</li>
     * </ul>
     *
     * @param manager the manager whose projects are to be displayed and managed
     */
    public static void viewHandledProjects(Manager manager) {
        List<Project> handledProjects = ProjectService.getProjectsByManager(manager);

        if (handledProjects.isEmpty()) {
            CommonView.displayMessage("You are not managing any projects.");
            return;
        }

        while (true) {
            CommonView.displayHeader("Projects Managed by You");
            ProjectView.displayProjectList(handledProjects);

            int projectChoice = CommonView.promptInt("Select project number to manage (or 0 to go back): ", 0, handledProjects.size());

            if (projectChoice == 0) {
                break;
            }

            Project selectedProject = handledProjects.get(projectChoice - 1);
            showProjectManagementMenu(selectedProject, manager);
        }
    }

    /**
     * Displays a project management menu for a selected project and manager.
     * <p>
     * The menu allows the manager to:
     * <ul>
     *   <li>Approve or reject officer registrations</li>
     *   <li>Manage applicant applications (not yet implemented)</li>
     *   <li>Edit project details (not yet implemented)</li>
     *   <li>View and reply to project-related enquiries</li>
     * </ul>
     *
     * @param project the project to manage
     * @param manager the manager handling the project
     */
    private static void showProjectManagementMenu(Project project, Manager manager) {
        boolean running = true;
        while (running) {
            int choice = ManagerView.showSelectHandledProjectMenu(project);
            switch (choice) {
                case 1:
                    manageProjectOfficerRegistration(project, manager);
                    break;
                case 2:
                    CommonView.displayMessage("Manage Applicant Applications - Not yet implemented.");
                    break;
                case 3:
                    CommonView.displayMessage("Manage Project Details - Not yet implemented.");
                    break;
                case 4:
                    EnquiryController.manageProjectEnquiries(java.util.Optional.empty(), java.util.Optional.of(manager), project);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    CommonView.displayError("Invalid choice.");
                    break;
            }
        }
    }

    /**
     * Allows a manager to manage officer registrations for a specific project.
     * <p>
     * Includes:
     * <ul>
     *   <li>Viewing pending and all registrations</li>
     *   <li>Approving or rejecting officer registration requests</li>
     * </ul>
     *
     * @param project the project for which officer registrations are managed
     * @param manager the manager approving or rejecting registrations
     */
    public static void manageProjectOfficerRegistration(Project project, Manager manager) {
        while (true) {
            List<Registration> allRegistrations = RegistrationService.getProjectRegistrations(project);
            List<Registration> pendingRegistrations = allRegistrations.stream()
                    .filter(r -> r.getRegistrationStatus() == RegistrationStatus.PENDING)
                    .collect(Collectors.toList());

            int choice = ManagerView.displayOfficerRegistrationsForApproval(allRegistrations, project);

            if (choice == 0) {
                break;
            }

            Registration selectedRegistration = pendingRegistrations.get(choice - 1);

            int action = ManagerView.promptApproveReject();
            boolean success = false;
            String officerName = selectedRegistration.getOfficer().getName();

            switch (action) {
                case 1:
                    success = RegistrationService.approveRegistration(selectedRegistration, manager);
                    if (success) {
                        ManagerView.displayRegistrationApprovedSuccess(officerName);
                    } else {
                        ManagerView.displayRegistrationActionFailed("approve");
                    }
                    break;
                case 2:
                    success = RegistrationService.rejectRegistration(selectedRegistration, manager);
                    if (success) {
                        ManagerView.displayRegistrationRejectedSuccess(officerName);
                    } else {
                        ManagerView.displayRegistrationActionFailed("reject");
                    }
                    break;
                case 0:
                    CommonView.displayMessage("Action cancelled.");
                    break;
                default:
                    CommonView.displayError("Invalid action.");
                    break;
            }

            if (action == 1 || action == 2) {
                CommonView.prompt("Press Enter to continue...");
            }
        }
    }

    /**
     * Allows the manager to create and save a new project.
     */

    public static void createProject() {
        throw new UnsupportedOperationException("Unimplemented method 'createProject'");
    }

    /**
     * Displays all projects and lets the manager view their details.
     */

    public static void viewAllProjects() {
        throw new UnsupportedOperationException("Unimplemented method 'viewAllProjects'");
    }

    /**
     * Allows the manager to view enquiries for any project.
     */
    public static void viewAllEnquiries() {
        throw new UnsupportedOperationException("Unimplemented method 'viewAllEnquiries'");
    }

    /**
     * Allows the manager to view all handled projects.
     */
    public static void viewHandledProjects() {
        throw new UnsupportedOperationException("Unimplemented method 'viewHandledProjects'");
    }

    /**
     * Displays detailed information for a specific project.
     *
     * @param project the project to display
     */
    public static void viewProjectDetails(Project project) {
        throw new UnsupportedOperationException("Unimplemented method 'viewProjectDetails'");
    }

    /**
     * Allows the manager to manage officer registrations to projects.
     */
    public static void manageProjectOfficerRegistration(Project project) {
        throw new UnsupportedOperationException("Unimplemented method 'manageProjectOfficerRegistration'");
    }
}

