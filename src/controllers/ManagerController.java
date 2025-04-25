package controllers;

import models.Enquiry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import models.Manager;
import models.Project;
import models.Registration;
import models.User;
import models.enums.ApplicationStatus;
import models.enums.EnquiryStatus;
import models.enums.RegistrationStatus;
import repositories.ProjectRepository;
import repositories.UserRepository;
import services.ApplicationService;
import services.EnquiryService;
import services.ProjectService;
import services.RegistrationService;
import services.ManagerService;
import views.CommonView;
import views.EnquiryView;
import views.ManagerView;
import views.ProjectView;
import models.Application;

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
    private final ManagerService managerService;
    private final ProjectService projectService;
    private final ApplicationService applicationService;
    private final RegistrationService registrationService;
    private final EnquiryService enquiryService;
    private final EnquiryController enquiryController;

    public ManagerController() {
        this.managerService = ManagerService.getInstance();
        this.projectService = ProjectService.getInstance();
        this.applicationService = ApplicationService.getInstance();
        this.registrationService = RegistrationService.getInstance();
        this.enquiryService = EnquiryService.getInstance();
        this.enquiryController = new EnquiryController();
    }

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
    public void viewHandledProjects(Manager manager) {
        List<Project> handledProjects = projectService.getProjectsByManager(manager);

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
    private void showProjectManagementMenu(Project project, Manager manager) {
        boolean running = true;
        while (running) {
            int choice = ManagerView.showSelectHandledProjectMenu(project);
            switch (choice) {
                case 1:
                    manageProjectOfficerRegistration(project, manager);
                    break;
                case 2: // Manage Applicant Applications
                    manageApplicantApplications(project, manager);
                    break;
                case 3: // Manage Project Details
                    editProjectDetails(project, manager);
                    break;
                case 4:
                    enquiryController.manageProjectEnquiries(java.util.Optional.empty(), java.util.Optional.of(manager), project);
                    break;
                case 5: // Generate Report
                    generateReport(project, manager);
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
    public void manageProjectOfficerRegistration(Project project, Manager manager) {
        while (true) {
            List<Registration> allRegistrations = registrationService.getProjectRegistrations(project);
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
                    success = registrationService.approveRegistration(selectedRegistration, manager);
                    if (success) {
                        ManagerView.displayRegistrationApprovedSuccess(officerName);
                    } else {
                        ManagerView.displayRegistrationActionFailed("approve");
                    }
                    break;
                case 2:
                    success = registrationService.rejectRegistration(selectedRegistration, manager);
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

    public void manageApplicantApplications(Project project, Manager manager) {
        while (true) {
            List<Application> applications = applicationService.getProjectApplications(project);
            ManagerView.displayApplicationList(applications, "Applications for Project: " + project.getProjectName());

            int choice = CommonView.promptInt("Select an application number to manage (or 0 to go back): ", 0, applications.size());

            if (choice == 0) {
                CommonView.displayMessage("Returning to project management menu.");
                break;
            } else {
                Application selectedApplication = applications.get(choice - 1);
                User applicant = UserRepository.getByNRIC(selectedApplication.getApplicantNRIC());
                String applicantName = (applicant != null) ? applicant.getName() : selectedApplication.getApplicantNRIC();
                ManagerView.displayApplicationDetails(selectedApplication);

                if (selectedApplication.isWithdrawalRequested()) {
                    int actionChoice = ManagerView.promptApproveRejectWithdrawal();
                    boolean success = false;
                    switch (actionChoice) {
                        case 1 -> { // Approve Withdrawal
                            if (CommonView.promptWordConfirmation(
                                    "Confirm APPROVAL of withdrawal request for application ID " + selectedApplication.getApplicationID() + "?", "APPROVE")) {
                                success = applicationService.approveWithdrawal(selectedApplication, manager);
                                if (success) {
                                    if (selectedApplication.getApplicationStatus() == ApplicationStatus.BOOKED) {
                                        project.incrementFlatCount(selectedApplication.getSelectedFlatType());
                                        ProjectRepository.saveAll();
                                    }
                                    ManagerView.displayWithdrawalApprovedSuccess(applicantName);
                                } else {
                                    ManagerView.displayWithdrawalActionFailed("approve");
                                }
                            }
                        }
                        case 2 -> { // Reject Withdrawal
                            if (CommonView.promptWordConfirmation(
                                    "Confirm REJECTION of withdrawal request for application ID " + selectedApplication.getApplicationID() + "?", "REJECT")) {
                                success = applicationService.rejectWithdrawal(selectedApplication, manager);
                                if (success) {
                                    ManagerView.displayWithdrawalRejectedSuccess(applicantName);
                                } else {
                                    ManagerView.displayWithdrawalActionFailed("reject");
                                }
                            }
                        }
                        case 0 -> CommonView.displayMessage("Action cancelled.");
                        default -> CommonView.displayError("Invalid action choice.");
                    }
                    if (actionChoice == 1 || actionChoice == 2) {
                        CommonView.prompt("Press Enter to continue...");
                    }
                } else if (selectedApplication.getApplicationStatus() == ApplicationStatus.PENDING) {
                    if (selectedApplication.canApprove()) {
                        int actionChoice = ManagerView.promptApproveReject();
                        boolean success = false;
                        switch (actionChoice) {
                            case 1 -> { // Approve Application
                                success = applicationService.approveApplication(selectedApplication, manager);
                                if (success) {
                                    CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " approved.");
                                } else {
                                    // Error message already shown by service if units are unavailable
                                    if (project.getAvailableUnits(selectedApplication.getSelectedFlatType()) > 0) {
                                         CommonView.displayError("Failed to approve application.");
                                    }
                                }
                            }
                            case 2 -> { // Reject Application
                                success = applicationService.rejectApplication(selectedApplication, manager);
                                if (success) {
                                    CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " rejected.");
                                } else {
                                    CommonView.displayError("Failed to reject application.");
                                }
                            }
                            case 0 -> CommonView.displayMessage("No action taken.");
                            default -> CommonView.displayError("Invalid action choice.");
                        }
                        if (actionChoice == 1 || actionChoice == 2) {
                            CommonView.prompt("Press Enter to continue...");
                        }
                    } else {
                        CommonView.displayMessage("This application cannot be approved/rejected as it has an active withdrawal request");
                        CommonView.prompt("Press Enter to continue...");
                    }
                } else {
                    CommonView.displayMessage("This application is in state: " + selectedApplication.getApplicationStatus().getDescription() +
                        " and has no actions available.");
                    CommonView.prompt("Press Enter to continue...");
                }
            }
        }
    }

    public void createProject(Manager manager) {
        CommonView.displayHeader("Create New BTO Project");
        String managerNRIC = manager.getUserNRIC();
        String projectName = ProjectView.getProjectName();
        String location = ProjectView.getProjectLocation();

        try {
            LocalDateTime startDate = CommonView.promptDate("Enter application opening date: ");
            LocalDateTime endDate = CommonView.promptDate("Enter application closing date: ");

            if (endDate.isBefore(startDate)) {
                CommonView.displayError("End date cannot be before start date.");
                return;
            }

            int officerSlots = ProjectView.getOfficerSlots();
            boolean visibility = ProjectView.getProjectVisibility();

            projectService.createProject(managerNRIC, projectName, location,
                startDate, endDate, officerSlots, visibility);
            ProjectView.displayProjectCreationSuccess(projectName);
        } catch (Exception e) {
            CommonView.displayError("Error creating project: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void editProjectDetails(Project project, Manager manager) {
        CommonView.displayHeader("Edit Project Details: " + project.getProjectName());
        boolean running = true;

        List<String> options = List.of(
            "Edit Project Name",
            "Edit Location",
            "Edit Application Opening Date",
            "Edit Application Closing Date",
            "Edit Officer Slots Amount",
            "Edit Project Visibility",
            "Delete Project"
        );

        while (running) {
            CommonView.displayShortSeparator();
            ProjectView.displayProjectDetailsOfficerView(project);

            int choice = CommonView.displayMenuWithBacking("Select detail to edit for " + project.getProjectName(), options);
            boolean changed = false;

            switch (choice) {
                case 1: // Edit Project Name
                    String newName = ProjectView.getProjectName();
                    project.setProjectName(newName);
                    changed = true;
                    CommonView.displaySuccess("Project name updated.");
                    break;
                case 2: // Edit Location
                    String newLocation = ProjectView.getProjectLocation();
                    project.setLocation(newLocation);
                    changed = true;
                    CommonView.displaySuccess("Project location updated.");
                    break;
                case 3: // Edit Application Opening Date
                    try {
                        LocalDateTime newStartDate = CommonView.promptDate("Enter new opening date: ");
                        if (newStartDate.isAfter(project.getApplicationCloseDate())) {
                            CommonView.displayError("Opening date cannot be after closing date.");
                        } else {
                            project.setApplicationOpenDate(newStartDate);
                            changed = true;
                            CommonView.displaySuccess("Application opening date updated.");
                        }
                    } catch (Exception e) {
                        CommonView.displayError("Invalid date format.");
                    }
                    break;
                case 4: // Edit Application Closing Date
                    try {
                        LocalDateTime newEndDate = CommonView.promptDate("Enter new closing date: ");
                        if (newEndDate.isBefore(project.getApplicationOpenDate())) {
                            CommonView.displayError("Closing date cannot be before opening date.");
                        } else {
                            project.setApplicationCloseDate(newEndDate);
                            changed = true;
                            CommonView.displaySuccess("Application closing date updated.");
                        }
                    } catch (Exception e) {
                        CommonView.displayError("Invalid date format.");
                    }
                    break;
                case 5: // Edit Officer Slots
                    int newSlots = ProjectView.getOfficerSlots();
                    project.setOfficerSlots(newSlots);
                    changed = true;
                    CommonView.displaySuccess("Officer slots updated.");
                    break;
                case 6: // Edit Project Visibility
                    boolean newVisibility = ProjectView.getProjectVisibility();
                    project.setVisible(newVisibility);
                    changed = true;
                    CommonView.displaySuccess("Project visibility updated.");
                    break;
                case 7: // Delete Project
                    if (deleteProject(project, manager)) {
                        return;
                    } else {
                        continue;
                    }
                case 0:
                    return;
                default:
                    CommonView.displayError("Invalid choice.");
                    break;
            }

            if (changed) {
                try {
                    projectService.updateProjectDetails(project);
                } catch (Exception e) {
                    CommonView.displayError("Error saving project details: " + e.getMessage());
                }
            }

            if (choice != 0) {
                 CommonView.prompt("Press Enter to continue editing...");
            }
        }
    }

    public boolean deleteProject(Project project, Manager manager) {
        CommonView.displayHeader("Delete Project: " + project.getProjectName());
        if (CommonView.promptWordConfirmation(
                "Are you sure you want to permanently delete project '" + project.getProjectName() + "'? This action cannot be undone.", "DELETE")) {
            try {
                projectService.deleteProject(project.getProjectName());
                ProjectView.displayProjectDeleteSuccess();
                return true;
            } catch (Exception e) {
                CommonView.displayError("Error deleting project: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Displays all projects and lets the manager view their details.
     */
    public void viewAllProjects() {
        CommonView.displayHeader("All BTO Projects");
        List<Project> allProjects = projectService.getAllProjects();
        if (allProjects.isEmpty()) {
            CommonView.displayMessage("There are no projects in the system.");
            return;
        }

        while (true) {
            CommonView.displayHeader("All Projects in System");
            ProjectView.displayProjectList(allProjects);
            int choice = CommonView.promptInt("Enter the number of the project to view details (or 0 to go back): ", 0, allProjects.size());

            if (choice == 0) {
                break;
            }

            Project selectedProject = allProjects.get(choice - 1);
            ProjectView.displayProjectDetailsManagerView(selectedProject);
            CommonView.prompt("Press Enter to return to the project list...");
        }
    }

    public void viewAllEnquiries(Manager manager) {
        List<Project> allProjects = projectService.getAllProjects();
        List<Enquiry> allEnquiries = new ArrayList<>();

        if (allProjects.isEmpty()) {
            CommonView.displayMessage("There are no projects in the system.");
            return;
        }

        for (Project project : allProjects) {
            allEnquiries.addAll(enquiryService.getProjectEnquiries(project));
        }

        if (allEnquiries.isEmpty()) {
            EnquiryView.displayEmptyMessage();
            return;
        }

        while (true) {
            CommonView.displayHeader("All Enquiries Across All Projects");

            EnquiryView.displayEnquiryList(allEnquiries);
            int choice = CommonView.promptInt("Enter the number of the enquiry to view/reply (or 0 to go back): ", 0, allEnquiries.size());

            if (choice == 0) {
                break;
            }

            Enquiry selectedEnquiry = allEnquiries.get(choice - 1);
            EnquiryView.displayEnquiry(selectedEnquiry);

            if (selectedEnquiry.getEnquiryStatus() == EnquiryStatus.RESPONDED) {
                CommonView.displayMessage("This enquiry has already been replied to.");
            } else if (selectedEnquiry.getEnquiryStatus() == EnquiryStatus.PENDING) {
                if (CommonView.promptYesNo("Do you want to reply to this enquiry?")) {
                    String reply = CommonView.prompt("Enter your reply: ");
                    if (reply != null && !reply.trim().isEmpty()) {
                        enquiryService.replyToEnquiry(selectedEnquiry, reply, manager.getUserNRIC());
                        EnquiryView.displaySuccess("Reply submitted successfully.");
                        CommonView.prompt("Press Enter to continue...");
                        break;
                    } else {
                        EnquiryView.displayError("Reply cannot be empty.");
                    }
                }
            }
            CommonView.prompt("Press Enter to return to the enquiry list...");
        }
    }

    public void generateReport(Project project, Manager manager) {
        CommonView.displayHeader("Generate Booked Applications Report for Project: " + project.getProjectName());

        Map<String, String> filters = ManagerView.promptFilterOptions();

        List<Map<String, String>> reportData = managerService.generateApplicantReport(project, filters);

        ManagerView.displayReport(reportData);

        if (!reportData.isEmpty() && ManagerView.promptExportToCsv()) {
            boolean running = true;
            while (running) {
                String filename = ManagerView.promptCsvFileName();
                if ((filename != null && !filename.trim().isEmpty())) {
                    managerService.exportReportToCsv(reportData, filename);
                    running = false;
                } else {
                    CommonView.displayError("Invalid filename. Please try again.");
                    CommonView.prompt("Press Enter to continue...");
                }
            }
        }
    }
}

