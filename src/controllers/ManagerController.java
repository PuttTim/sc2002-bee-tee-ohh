package controllers;

import java.util.List;
import java.util.stream.Collectors;

import models.Manager;
import models.Project;
import models.Registration;
import models.User;
import models.enums.ApplicationStatus;
import models.enums.RegistrationStatus;
import repositories.UserRepository;
import services.ApplicationService;
import services.ProjectService;
import services.RegistrationService;
import views.CommonView;
import views.ManagerView;
import views.ProjectView;
import models.Application;

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
                     // TODO: Implement project details editing
                    CommonView.displayMessage("Manage Project Details - Not yet implemented.");
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

                if (selectedApplication.getApplicationStatus() == ApplicationStatus.WITHDRAWAL_REQUESTED) {
                    int actionChoice = ManagerView.promptApproveRejectWithdrawal();
                    boolean success = false;
                    switch (actionChoice) {
                        case 1: // Approve Withdrawal
                            success = ApplicationService.approveWithdrawal(selectedApplication, manager);
                            if (success) {
                                ManagerView.displayWithdrawalApprovedSuccess(applicantName);
                            } else {
                                ManagerView.displayWithdrawalActionFailed("approve");
                            }
                            break;
                        case 2: // Reject Withdrawal
                            success = ApplicationService.rejectWithdrawal(selectedApplication, manager);
                            if (success) {
                                ManagerView.displayWithdrawalRejectedSuccess(applicantName);
                            } else {
                                ManagerView.displayWithdrawalActionFailed("reject");
                            }
                            break;
                        case 0: // Cancel
                            CommonView.displayMessage("Action cancelled.");
                            break;
                        default:
                            CommonView.displayError("Invalid action choice.");
                            break;
                    }
                    if (actionChoice == 1 || actionChoice == 2) {
                        CommonView.prompt("Press Enter to continue...");
                    }
                } else if (selectedApplication.getApplicationStatus() == ApplicationStatus.PENDING) {
                    if (selectedApplication.canApprove()) {
                        int actionChoice = ManagerView.promptApproveReject();
                        boolean success = false;
                        switch (actionChoice) {
                            case 1: // Approve Application
                                success = ApplicationService.approveApplication(selectedApplication, manager);
                                if (success) {
                                    CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " approved.");
                                } else {
                                    // Error message already shown by service if units are unavailable
                                    if (project.getAvailableUnits(selectedApplication.getSelectedFlatType()) > 0) {
                                         CommonView.displayError("Failed to approve application.");
                                    }
                                }
                                break;
                            case 2: // Reject Application
                                success = ApplicationService.rejectApplication(selectedApplication, manager);
                                if (success) {
                                    CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " rejected.");
                                } else {
                                    CommonView.displayError("Failed to reject application.");
                                }
                                break;
                            case 0: // Cancel
                                CommonView.displayMessage("No action taken.");
                                break;
                            default:
                                CommonView.displayError("Invalid action choice.");
                                break;
                        }
                         if (actionChoice == 1 || actionChoice == 2) {
                            CommonView.prompt("Press Enter to continue...");
                        }
                    } else {
                        CommonView.displayMessage("This application is PENDING but cannot be approved/rejected currently (e.g., withdrawal requested).");
                        CommonView.prompt("Press Enter to continue...");
                    }
                } else {
                    CommonView.displayMessage("This application is not in a state that requires manager action (Status: " + selectedApplication.getApplicationStatus() + ").");
                    CommonView.prompt("Press Enter to continue...");
                }
            }
        }
    }

    public static void createProject() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProject'");
    }

    public static void viewAllProjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'viewAllProjects'");
    }

    public static void viewAllEnquiries() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'viewAllEnquiries'");
    }

    // method to display all projects which then will ask the user to select a project
    public static void viewHandledProjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'viewHandledProjects'");
    }

    // this will show a menu of actions that the manager can do on the project
    public static void viewProjectDetails(Project project) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'viewProjectDetails'");
    }

    public static void manageProjectOfficerRegistration(Project project) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'manageProjectOfficerRegistration'");
    }
}
