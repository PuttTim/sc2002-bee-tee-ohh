package controllers;

import models.Enquiry;
import java.time.LocalDateTime;
import java.util.List;
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
import repositories.OfficerRepository;
import repositories.ProjectRepository;
import services.ProjectService;
import services.RegistrationService;
import views.CommonView;
import views.EnquiryView;
import views.ManagerView;
import views.ProjectView;
import models.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerController {


    // Method to display all projects managed by the manager, then ask the user to select a project
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

        return;
    }

    // Shows the menu for actions on a specific project managed by the manager
    private static void showProjectManagementMenu(Project project, Manager manager) {
         boolean running = true;
         while(running) {
            int choice = ManagerView.showSelectHandledProjectMenu(project); 
            switch (choice) {
                case 1: // Manage Officer Registrations
                    manageProjectOfficerRegistration(project, manager);
                    break;
                case 2: // Manage Applicant Applications
                    manageApplicantApplications(project, manager); // Updated call
                    break;
                case 3: // Manage Project Details 
                    editProjectDetails(project, manager);
                    break;
                case 4: // View Enquiries
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


    // Manages officer registrations for a specific project
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
                case 1: // Approve
                    success = RegistrationService.approveRegistration(selectedRegistration, manager);
                    if (success) {
                        ManagerView.displayRegistrationApprovedSuccess(officerName);
                    } else {
                        ManagerView.displayRegistrationActionFailed("approve");
                    }
                    break;
                case 2: // Reject
                    success = RegistrationService.rejectRegistration(selectedRegistration, manager);
                     if (success) {
                        ManagerView.displayRegistrationRejectedSuccess(officerName);
                    } else {
                        ManagerView.displayRegistrationActionFailed("reject");
                    }
                    break;
                case 0: // Cancel
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

    public static void manageApplicantApplications(Project project, Manager manager) {
        while (true) {
            List<Application> applications = ApplicationService.getProjectApplications(project);
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
                            success = ApplicationService.approveWithdrawal(selectedApplication, manager);
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
                        case 2 -> { // Reject Withdrawal
                            success = ApplicationService.rejectWithdrawal(selectedApplication, manager);
                            if (success) {
                                ManagerView.displayWithdrawalRejectedSuccess(applicantName);
                            } else {
                                ManagerView.displayWithdrawalActionFailed("reject");
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
                                success = ApplicationService.approveApplication(selectedApplication, manager);
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
                                success = ApplicationService.rejectApplication(selectedApplication, manager);
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

    public static void createProject(Manager manager) {
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

            ProjectService.createProject(managerNRIC, projectName, location, 
                startDate, endDate, officerSlots, visibility);
            System.out.println("PROJECT CREATE 4");
            ProjectView.displayProjectCreationSuccess(projectName);
        } catch (Exception e) {
            CommonView.displayError("Error creating project: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void editProjectDetails(Project project, Manager manager) {
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
                    ProjectService.updateProjectDetails(project);
                } catch (Exception e) {
                    CommonView.displayError("Error saving project details: " + e.getMessage());
                }
            }

            if (choice != 0) { 
                 CommonView.prompt("Press Enter to continue editing...");
            }
        }
    }

    public static boolean deleteProject(Project project, Manager manager) {
        CommonView.displayHeader("Delete Project: " + project.getProjectName());
        if (CommonView.promptYesNo("Are you sure you want to delete this project? This action cannot be undone.")) {
            try {
                ProjectService.deleteProject(project.getProjectName());
                ProjectView.displayProjectDeleteSuccess();
                return true;
            } catch (Exception e) {
                CommonView.displayError("Error deleting project: " + e.getMessage());
            }
        } else {
            CommonView.displayMessage("Project deletion cancelled.");
        }
        return false;
    }

    public static void viewAllProjects() {
        CommonView.displayHeader("All BTO Projects");
        List<Project> allProjects = ProjectService.getAllProjects();
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
        ProjectView.displayProjectList(allProjects);
        CommonView.prompt("Press Enter to return to the main menu...");
    }

    public static void viewAllEnquiries(Manager manager) {
        List<Project> allProjects = ProjectService.getAllProjects(); 
        List<Enquiry> allEnquiries = new ArrayList<>();

        if (allProjects.isEmpty()) {
            CommonView.displayMessage("There are no projects in the system.");
            return;
        }

        for (Project project : allProjects) {
            allEnquiries.addAll(EnquiryService.getProjectEnquiries(project));
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
                        EnquiryService.replyToEnquiry(selectedEnquiry, reply, manager.getUserNRIC());
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
}
