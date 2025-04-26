package controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

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
import repositories.ReceiptRepository;
import repositories.RegistrationRepository;
import services.*;
import views.*;
import interfaces.IOfficerService;

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
    private final ProjectService projectService;
    private final OfficerService officerService;
    private final EnquiryController enquiryController;
    private final ApplicationService applicationService;
    
    public OfficerController() {
        this.projectService = ProjectService.getInstance();
        this.officerService = OfficerService.getInstance();
        this.enquiryController = new EnquiryController();
        this.applicationService = ApplicationService.getInstance();
    }

    /**
     * <p>Registers an officer to handle a project.</p>
     * <ul>
     *   <li>Displays available projects.</li>
     *   <li>Allows officer to select and register for a project.</li>
     *   <li>Checks for conflicts, such as existing registrations or overlapping timelines.</li>
     * </ul>
     * @param officer The officer attempting to register for a project.
     */
    public void registerToHandleProject(Officer officer) {
        List<Project> projects = officerService.getAvailableProjects();
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available for registration.");
            return;
        }

        while (true) {
            ProjectView.displayOfficerRegistrations(projects, RegistrationRepository.getByOfficer(officer), officer);
            int projectChoice = CommonView.promptInt("Select project number (or 0 to cancel): ", 0, projects.size());

            if (projectChoice == 0) {
                CommonView.displayMessage("Registration cancelled.");
                break;
            }

            Project selectedProject = projects.get(projectChoice - 1);
            if (!officerService.isProjectEligibleForRegistration(selectedProject, officer)) {
                CommonView.displayError("You are not eligible to register for this project.");
                continue;
            }

            officerService.registerOfficerForProject(officer, selectedProject);
            CommonView.displayMessage("You have successfully registered to handle project: " + selectedProject.getProjectName() + "! Please wait for approval.");
            break;
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
    public void checkHandlerRegistration(Officer officer) {
        if (officerService.hasExistingProject(officer)) {
            Project project = officerService.getProjectByOfficer(officer);
            if (project != null) {
                CommonView.displayMessage("You are currently handling project: " + project.getProjectName());
            }
        } else if (officerService.hasExistingRegistration(officer)) {
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
    public void viewHandledProjectDetails(Officer officer) {
        List<Project> projects = officerService.getHandledProjects(officer);
        int projectChoice = ProjectView.displayAndFilterProjects(projects, "Handled Projects");

        if (projectChoice == 0) {
            CommonView.displayMessage("Returning to project selection.");
            return;
        }

        Project selectedProject = projects.get(projectChoice - 1);


        CommonView.displayHeader("Project Details for " + selectedProject.getProjectName());
        ProjectView.displayProjectDetailsOfficerView(selectedProject);

        boolean runningProjectMenu = true;
        while (runningProjectMenu) {
            int choice = OfficerView.showSelectHandledProjectMenu(selectedProject);

            switch (choice) {
                case 1 -> { // Manage Applications
                    manageProjectApplications(selectedProject, officer);
                }
                case 2 -> { // Manage Successful Applications
                    manageSuccessfulApplications(selectedProject, officer);
                }
                case 3 -> { // View Enquiries
                    manageProjectEnquiries(selectedProject, officer);
                }
                case 0 -> {
                    CommonView.displayMessage("Returning to project selection.");
                    runningProjectMenu = false;
                }
                default -> CommonView.displayError("Invalid choice.");
            }
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
    public void manageProjectEnquiries(Project project, Officer officer) {
        enquiryController.manageProjectEnquiries(Optional.of(officer), Optional.empty(), project);
    }

    /**
     * <p>Manages applications for a specific project.</p>
     * <ul>
     *   <li>Displays a list of applications for the project.</li>
     * </ul>
     * @param project The project whose applications are being managed.
     * @param officer The officer managing the applications.
     */
    public void manageProjectApplications(Project project, Officer officer) {
        List<Application> applications = applicationService.getProjectApplications(project);
        OfficerView.displayApplicationList(applications, "All Applications for Project: " + project.getProjectName());
        CommonView.prompt("Press Enter to return to the project menu...");
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
    public void manageSuccessfulApplications(Project project, Officer officer) {
        while (true) {
            List<Application> relevantApplications = applicationService.getProjectApplications(project).stream()
                .filter(app -> app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL || app.getApplicationStatus() == ApplicationStatus.BOOKED)
                .collect(Collectors.toList());
            
            if (relevantApplications.isEmpty()) {
                CommonView.displayMessage("No successful or booked applications found for this project.");
                CommonView.prompt("Press Enter to return to the project menu...");
                return;
            }

            OfficerView.displayApplicationList(relevantApplications, "Successful/Booked Applications for Project: " + project.getProjectName());
            
            boolean canBookAny = relevantApplications.stream().anyMatch(app -> app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL);

            if (!canBookAny) {
                CommonView.displayMessage("No applications are currently available for booking (all are already booked).");
                CommonView.prompt("Press Enter to return to the project menu...");
                break;
            }

            
            int choice = CommonView.promptInt("Select a SUCCESSFUL application number to book (or 0 to go back): ", 0, relevantApplications.size());

            if (choice == 0) {
                CommonView.displayMessage("Returning to project menu.");
                break;
            } else {
                Application selectedApplication = relevantApplications.get(choice - 1);
                OfficerView.displayApplicationDetails(selectedApplication);

            
                if (selectedApplication.getApplicationStatus() == ApplicationStatus.BOOKED) {
                    CommonView.displayMessage("This application has already been booked.");
                    if (CommonView.promptYesNo("Do you want to view the receipt?")) {
                        ReceiptRepository.getByApplicantNRIC(selectedApplication.getApplicantNRIC()).stream()
                            .filter(r -> r.getProjectName().equals(project.getProjectName())) // Ensure receipt matches project
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
                     CommonView.displayError("This application cannot be booked (Status: " + selectedApplication.getApplicationStatus() + "). Only SUCCESSFUL applications can be booked.");
                     CommonView.prompt("Press Enter to continue...");
                     continue;
                }

                if (CommonView.promptYesNo("Do you want to book this application? (This will update status to BOOKED and reduce flat count)")) {
                    String unitNumber = CommonView.prompt("Enter the unit number assigned (e.g., 04-001): ");
                    if (unitNumber == null || unitNumber.trim().isEmpty() || !unitNumber.matches("\\d{2}-\\d{3}")) {
                        CommonView.displayError("Invalid unit number format (expected XX-XXX). Booking cancelled.");
                        continue;
                    }

                    boolean booked = applicationService.bookApplication(selectedApplication, officer, unitNumber);
                    
                    if (booked) {
                        CommonView.displaySuccess("Application ID " + selectedApplication.getApplicationID() + " successfully booked for unit " + unitNumber + ".");
                        generateReceiptForBooking(selectedApplication, officer, unitNumber, project); 
                    } else {
                        if (project.getAvailableUnits(selectedApplication.getSelectedFlatType()) <= 0) {
                             CommonView.displayError("Booking failed: No available units left for this flat type.");
                        } else {
                            CommonView.displayError("Failed to book application. It might have been booked by another officer, or an unexpected error occurred.");
                        }
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
    private void generateReceiptForBooking(Application application, Officer officer, String unitNumber, Project project) {
        Applicant applicant = ApplicantRepository.getByNRIC(application.getApplicantNRIC());

        if (applicant == null) {
            applicant = OfficerRepository.getByNRIC(application.getApplicantNRIC());
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
