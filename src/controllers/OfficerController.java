package controllers;

import java.util.List;
import java.util.stream.Collectors;

import models.Applicant;
import models.Application;
import models.Enquiry;
import models.Officer;
import models.Project;
import models.Receipt;
import models.Registration;
import models.enums.ApplicationStatus;
import models.enums.RegistrationStatus;
import repositories.ApplicantRepository;
import repositories.ApplicationRepository;
import repositories.EnquiryRepository;
import repositories.OfficerRepository;
import repositories.ProjectRepository;
import repositories.ReceiptRepository;
import repositories.RegistrationRepository;
import repositories.UserRepository;
import services.*;
import views.*;

import services.OfficerService;
import services.ProjectService;

import views.CommonView;
import views.ReceiptView;

/**
 * <p>Handles all operations related to Officer actions in the housing project management system.</p>
 * <ul>
 *   <li>Registers officers to handle projects.</li>
 *   <li>Manages project applications, including approval/rejection.</li>
 *   <li>Handles project enquiries and responses.</li>
 *   <li>Manages successful applications, including booking.</li>
 *   <li>Generates receipts for booked applications.</li>
 * </ul>
 */
public class OfficerController {
    //register to join project as officer
    /**
     * <p>Registers an officer to handle a project.</p>
     * <ul>
     *   <li>Displays available projects.</li>
     *   <li>Allows officer to select and register for a project.</li>
     *   <li>Checks for conflicts, such as existing registrations or overlapping timelines.</li>
     * </ul>
     * @param officer The officer attempting to register for a project.
     */
    public static void registerToHandleProject(Officer officer) {
        List<Project> projects = ProjectService.getVisibleProjects();
        List<Registration> officerRegistrations = RegistrationRepository.getByOfficer(officer);
        List<Project> registeredProjects = projects.stream()
                .filter(p -> officerRegistrations.stream().filter(r -> r.getRegistrationStatus() != RegistrationStatus.REJECTED).anyMatch(r -> r.getProjectID().equals(p.getProjectID())))
                .collect(Collectors.toList());
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available for registration.");
            return;
        }

        int projectChoice = -1;

        while (true) {
            ProjectView.displayOfficerRegistrations(projects, officerRegistrations, officer);
            projectChoice = CommonView.promptInt("Select project number (or 0 to cancel): ", 0, projects.size());

            if (projectChoice == 0) {
                break;
            }

            Project project = projects.get(projectChoice - 1);
            if (project.getOfficerSlots() <= project.getOfficers().size()) {
                CommonView.displayError("No available slots for this project.");
                continue;
            }

            if (ApplicationRepository.hasApplication(officer, project.getProjectID())) {
                CommonView.displayError("You have an existing BTO application for this project.");
                continue;
            }

            if (officerRegistrations.stream().anyMatch(r -> r.getProjectID().equals(project.getProjectID()))) {
                CommonView.displayError("You have an existing registration for this project.");
                continue;
            }

            if (registeredProjects.stream().anyMatch(r -> r.getApplicationOpenDate().isBefore(project.getApplicationCloseDate()) &&
                    r.getApplicationCloseDate().isAfter(project.getApplicationOpenDate()))) {
                CommonView.displayError("You have an existing registration that overlaps with this project's timeline.");
                continue;
            }

            break;
        }

        System.out.println("Selected project: " + (projectChoice != 0 ? projects.get(projectChoice - 1).getProjectName() : "None"));

        if (projectChoice != 0) {
            Project selectedProject = projects.get(projectChoice - 1);
            Registration registration = new Registration(officer, selectedProject.getProjectID());
            RegistrationRepository.add(registration);
            CommonView.displayMessage("You have successfully registered to handle project: " + selectedProject.getProjectName() + "! Please wait for approval.");
        } else {
            CommonView.displayMessage("Registration cancelled.");
        }
    }

    /**
     * <p>Checks the current registration status of an officer.</p>
     * <ul>
     *   <li>Displays if the officer is currently handling a project.</li>
     *   <li>Shows if there is a pending registration request.</li>
     *   <li>Notifies if the officer has no active registrations.</li>
     * </ul>
     * @param officer The officer whose registration status is being checked.
     */
    public static void checkHandlerRegistration(Officer officer) {
        if (OfficerRepository.hasExistingProject(officer)) {
            Project project = ProjectService.getProjectByOfficer(officer);
            if (project != null) {
                CommonView.displayMessage("You are currently handling project: " + project.getProjectName());
            }
        } else if (OfficerService.hasExistingRegistration(officer)) {
            CommonView.displayMessage("You have a pending registration request.");
        } else {
            CommonView.displayMessage("You have no active registrations.");
        }
    }

    /**
     * <p>Allows an officer to view the details of projects they are handling.</p>
     * <ul>
     *   <li>Displays list of projects the officer is handling.</li>
     *   <li>Allows officer to select a project and view its details.</li>
     *   <li>Provides options to manage applications, successful applications, and enquiries.</li>
     * </ul>
     * @param officer The officer viewing the project details.
     */
    public static void viewHandledProjectDetails(Officer officer) {
        List<Project> projects = ProjectService.getAllOfficersProjects(officer.getUserNRIC());
        if (projects != null && !projects.isEmpty()) {
            while (true) {
                OfficerView.displayOfficerHandledProjects(projects);

                int projectChoice = CommonView.promptInt("Select project number to view details (or 0 to cancel): ", 0, projects.size());

                if (projectChoice == 0) {
                    CommonView.displayMessage("Cancelled viewing project details.");
                    break;
                }

                Project selectedProject = projects.get(projectChoice - 1);

                CommonView.displayHeader("Project Details for " + selectedProject.getProjectName());
                ProjectView.displayProjectDetailsOfficerView(selectedProject);

                boolean runningProjectMenu = true;
                while (runningProjectMenu) {
                    int choice = OfficerView.showSelectHandledProjectMenu(selectedProject);

                    switch (choice) {
                        case 1 -> {
                            manageProjectApplications(selectedProject, officer);
                        }
                        case 2 -> {
                            manageSuccessfulApplications(selectedProject, officer);
                        }
                        case 3 -> {
                            manageProjectEnquiries(selectedProject, officer);
                        }
                        case 0 -> {
                            CommonView.displayMessage("Returning to project selection.");
                            runningProjectMenu = false;
                        }
                    }
                }
            }

        } else {
            CommonView.displayError("You do not have any projects assigned to you.");
        }
    }

    /**
     * <p>Manages project enquiries for a specific project.</p>
     * <ul>
     *   <li>Displays a list of enquiries for the project.</li>
     *   <li>Allows officer to respond to enquiries.</li>
     * </ul>
     * @param project The project whose enquiries are being managed.
     * @param officer The officer responding to the enquiries.
     */
    public static void manageProjectEnquiries(Project project, Officer officer) {
        while (true) {
            List<Enquiry> enquiries = EnquiryService.getProjectEnquiries(project);
            ProjectController.viewProjectEnquiries(project);
            int choice = CommonView.promptInt("Select an enquiry to reply to (or 0 to cancel): ", 0, enquiries.size());

            if (choice == 0) {
                CommonView.displayMessage("Cancelled replying to enquiries.");
                break;
            } else {
                Enquiry selectedEnquiry = enquiries.get(choice - 1);
                String response = CommonView.prompt("Enter your response: ");
                if (response != null && !response.trim().isEmpty()) {
                    selectedEnquiry.markAsResponded(officer.getUserNRIC(), response);
                    EnquiryRepository.update(selectedEnquiry);
                    CommonView.displayMessage("Enquiry Response sent successfully.");
                    continue;
                } else {
                    CommonView.displayError("Response cannot be empty.");
                }

            }
        }

    }

    /**
     * <p>Manages applications for a specific project.</p>
     * <ul>
     *   <li>Displays a list of applications for the project.</li>
     *   <li>Allows officer to approve or reject applications.</li>
     *   <li>Handles application withdrawal requests and checks if the application can be approved.</li>
     * </ul>
     * @param project The project whose applications are being managed.
     * @param officer The officer managing the applications.
     */
    public static void manageProjectApplications(Project project, Officer officer) {
        while (true) {
            List<Application> applications = ApplicationService.getProjectApplications(project);
            OfficerView.displayApplicationList(applications, "Applications for Project: " + project.getProjectName());

            int choice = CommonView.promptInt("Select an application number to manage (or 0 to go back): ", 0, applications.size());

            if (choice == 0) {
                CommonView.displayMessage("Returning to officer menu.");
                break;
            } else {
                Application selectedApplication = applications.get(choice - 1);
                OfficerView.displayApplicationDetails(selectedApplication);

                if (selectedApplication.getApplicationStatus() == ApplicationStatus.PENDING) {
                    if (selectedApplication.canApprove()) {
                        int actionChoice = CommonView.promptInt("Action: [1] Approve [2] Reject [0] Back: ", 0, 2);
                        switch (actionChoice) {
                            case 1 -> {
                                if (ApplicationService.approveApplication(selectedApplication, officer)) {
                                    CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " approved.");
                                } else {
                                    CommonView.displayError("Failed to approve application.");
                                }
                                CommonView.prompt("Press Enter to continue...");
                            }
                            case 2 -> {
                                if (ApplicationService.rejectApplication(selectedApplication, officer)) {
                                    CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " rejected.");
                                } else {
                                    CommonView.displayError("Failed to reject application.");
                                }
                                CommonView.prompt("Press Enter to continue...");
                            }
                            case 0 -> CommonView.displayMessage("No action taken.");
                            default -> CommonView.displayError("Invalid action choice.");
                        }
                    } else if (selectedApplication.isWithdrawalRequested()) {
                         CommonView.displayMessage("This application has a pending withdrawal request and cannot be approved/rejected.");
                         CommonView.prompt("Press Enter to continue...");
                    } else {
                        CommonView.displayMessage("This application is PENDING but cannot be approved/rejected currently.");
                        CommonView.prompt("Press Enter to continue...");
                    }
                } else {
                    CommonView.prompt("Press Enter to continue...");
                }
            }
        }
    }

    /**
     * <p>Manages successful applications for a specific project.</p>
     * <ul>
     *   <li>Displays a list of successful applications.</li>
     *   <li>Allows officer to book the successful applications.</li>
     *   <li>Generates receipts if the application is successfully booked.</li>
     * </ul>
     * @param project The project whose successful applications are being managed.
     * @param officer The officer managing the successful applications.
     */
    public static void manageSuccessfulApplications(Project project, Officer officer) {
        while (true) {
            List<Application> successfulApplications = ApplicationService.getSuccessfulProjectApplications(project);

            if (successfulApplications.isEmpty()) {
                CommonView.displayMessage("No successful applications available to book for this project.");
                return;
            }

            OfficerView.displayApplicationList(successfulApplications, "Successful Applications for Project: " + project.getProjectName());

            int choice = CommonView.promptInt("Select an application number to book (or 0 to go back): ", 0, successfulApplications.size());

            if (choice == 0) {
                CommonView.displayMessage("Returning to officer menu.");
                break;
            } else {
                Application selectedApplication = successfulApplications.get(choice - 1);
                OfficerView.displayApplicationDetails(selectedApplication);

                if (selectedApplication.getApplicationStatus() == ApplicationStatus.BOOKED) {
                    CommonView.displayMessage("This application has already been booked.");
                    if (CommonView.promptYesNo("Do you want to view the receipt?")) {
                        ReceiptRepository.getByApplicantNRIC(selectedApplication.getApplicantNRIC()).stream()
                            .filter(r -> r.getProjectName().equals(project.getProjectName()))
                            .findFirst()
                            .ifPresentOrElse(
                                ReceiptView::displayReceiptDetails,
                                () -> CommonView.displayError("Receipt not found for this booking.")
                            );
                    }
                    CommonView.prompt("Press Enter to continue...");
                    continue;
                }

                if (selectedApplication.getApplicationStatus() != ApplicationStatus.SUCCESSFUL) {
                     CommonView.displayError("This application is not in a state that can be booked (Status: " + selectedApplication.getApplicationStatus() + ").");
                     CommonView.prompt("Press Enter to continue...");
                     continue;
                }

                if (CommonView.promptYesNo("Do you want to book this application? (This will update status to BOOKED and reduce flat count)")) {
                    String unitNumber = CommonView.prompt("Enter the unit number assigned (e.g., 04-001): ");
                    if (unitNumber == null || unitNumber.trim().isEmpty() || !unitNumber.matches("\\d{2}-\\d{3}")) {
                        CommonView.displayError("Invalid unit number format (expected XX-XXX). Booking cancelled.");
                        continue;
                    }

                    boolean booked = ApplicationService.bookApplication(selectedApplication, officer, unitNumber);

                    if (booked) {
                        CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " successfully booked for unit " + unitNumber + ".");
                        generateReceiptForBooking(selectedApplication, officer, unitNumber, project);
                    } else if (project.getAvailableUnits(selectedApplication.getSelectedFlatType()) <= 0) {
                        CommonView.displayError("No available units left for this flat type.");
                        if (CommonView.promptYesNo("Do you want to update the application status to UNSUCCESSFUL? ")) {
                            if (ApplicationService.rejectApplication(selectedApplication, officer)) {
                                CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " status updated to UNSUCCESSFUL.");
                            } else {
                                CommonView.displayError("Failed to update application status. It might no longer be in a rejectable state.");
                            }
                        } else {
                            CommonView.displayMessage("No action taken.");
                        }
                    } else {
                         CommonView.displayError("Failed to book application. Please check application status and available units, then try again.");
                    }
                    CommonView.prompt("Press Enter to continue...");
                } else {
                    CommonView.displayMessage("Booking cancelled.");
                }
            }
        }
    }

    /**
     * <p>Generates a receipt for a successful booking of an application.</p>
     * <ul>
     *   <li>Creates a receipt with the applicant's details and project information.</li>
     *   <li>Saves the receipt in the database and displays it.</li>
     * </ul>
     * @param application The application being booked.
     * @param officer The officer managing the booking.
     * @param unitNumber The assigned unit number for the application.
     * @param project The project in which the application is being booked.
     */
    private static void generateReceiptForBooking(Application application, Officer officer, String unitNumber, Project project) {
        Applicant applicant = ApplicantRepository.getByNRIC(application.getApplicantNRIC());

        if (applicant == null) {
            CommonView.displayError("Critical Error: Could not find applicant details for NRIC " + application.getApplicantNRIC() + ". Receipt not generated.");
            return;
        }

        Receipt receipt = new Receipt(
            applicant.getName(),
            applicant.getUserNRIC(),
            applicant.getAge(),
            applicant.getMaritalStatus(),
            application.getSelectedFlatType(),
            project.getFlatPrice(application.getSelectedFlatType()),
            unitNumber,
            project.getProjectName(),
            project.getProjectID(),
            project.getLocation(),
            officer
        );

        ReceiptRepository.add(receipt);
        ReceiptView.displayReceiptGeneratedSuccess(receipt.getReceiptId());
        ReceiptView.displayReceiptDetails(receipt);
    }
}
