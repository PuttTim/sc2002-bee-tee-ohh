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
import repositories.ReceiptRepository;
import repositories.RegistrationRepository;
import services.*;
import views.*;

import services.OfficerService;
import services.ProjectService;

import views.CommonView;
import views.ReceiptView;

public class OfficerController {
    //register to join project as officer
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
