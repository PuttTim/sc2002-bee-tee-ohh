package controllers;

import java.util.List;

import models.Applicant;
import models.Application;
import models.Project;
import models.enums.ApplicationStatus;

import services.ApplicantApplicationService;
import services.ApplicationService;
import services.ProjectService;
import views.ApplicantApplicationView;
import views.CommonView;

/**
 * Controller class that is responsible for handling applicant-related actions.
 * <p>Applicant-related actions include:</p>
 * <ul>
 *     <li>Making new applications</li>
 *     <li>Viewing existing application statuses</li>
 * </ul>
 */
public class ApplicantController {
    private final ProjectService projectService;
    private final ApplicationService applicationService;
    private final ApplicantApplicationService applicantApplicationService;

    public ApplicantController() {
        this.projectService = ProjectService.getInstance();
        this.applicationService = ApplicationService.getInstance();
        this.applicantApplicationService = ApplicantApplicationService.getInstance();
    }

    public void manageApplications(Applicant applicant) {
        boolean running = true;
        while (running) {
            int choice = ApplicantApplicationView.showMainMenu();
            try {
                switch (choice) {
                    case 1 -> viewApplications(applicant);
                    case 2 -> viewAvailableProjects(applicant);
                    case 3 -> submitNewApplication(applicant);
                    case 4 -> handleWithdrawal(applicant);
                    case 0 -> running = false;
                    default -> CommonView.displayError("Invalid choice!");
                }
            } catch (Exception e) {
                CommonView.displayError("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Allows the applicant to view current application statuses and application menu options.
     *
     * @param applicant the applicant who has applied for a BTO project.
     */
    private void viewApplications(Applicant applicant) {
        List<Application> applications = applicantApplicationService.getApplicationsByApplicant(applicant);
        ApplicantApplicationView.displayApplicationList(applications, "Your Applications");
        
        if (!applications.isEmpty()) {
            int choice = CommonView.promptInt("Select an application to view details (or 0 to go back): ", 0, applications.size());
            if (choice > 0) {
                Application selectedApp = applications.get(choice - 1);
                ApplicantApplicationView.displayApplicationDetails(selectedApp);
                CommonView.prompt("Press Enter to continue...");
            }
        } else {
            CommonView.prompt("Press Enter to continue...");
        }
    }

    public void viewAvailableProjects(Applicant applicant) {
        List<Project> eligibleProjects = applicantApplicationService.getEligibleProjects(applicant);
        ApplicantApplicationView.displayEligibleProjects(eligibleProjects);
        CommonView.prompt("Press Enter to continue...");
    }

    /**
     * Starts a new application for the applicant.
     *
     * @param applicant the applicant who is applying for a BTO project.
     */
    private void submitNewApplication(Applicant applicant) {
        List<Project> eligibleProjects = applicantApplicationService.getEligibleProjects(applicant);
        if (eligibleProjects.isEmpty()) {
            CommonView.displayMessage("No eligible projects available for application.");
            return;
        }

        int projectChoice = ApplicantApplicationView.promptProjectSelection(eligibleProjects);
        if (projectChoice == 0) return;

        Project selectedProject = eligibleProjects.get(projectChoice - 1);
        int flatTypeChoice = ApplicantApplicationView.promptFlatTypeSelection(selectedProject);
        
        try {
            boolean success = applicantApplicationService.submitApplication(
                applicant, 
                selectedProject, 
                selectedProject.getFlatTypes().get(flatTypeChoice - 1)
            );
            
            if (success) {
                ApplicantApplicationView.displaySubmissionSuccess();
            } else {
                ApplicantApplicationView.displaySubmissionError("You have an existing BTO application. Please try again later.");
            }
        } catch (Exception e) {
            ApplicantApplicationView.displaySubmissionError(e.getMessage());
        }
        CommonView.prompt("Press Enter to continue...");
    }

    private void handleWithdrawal(Applicant applicant) {
        List<Application> applications = applicantApplicationService.getApplicationsByApplicant(applicant)
            .stream()
            .filter(app -> app.getApplicationStatus() == ApplicationStatus.PENDING && !app.isWithdrawalRequested())
            .toList();

        ApplicantApplicationView.displayApplicationList(applications, "Applications Available for Withdrawal");
        
        if (applications.isEmpty()) {
            CommonView.displayMessage("No applications available for withdrawal.");
            CommonView.prompt("Press Enter to continue...");
            return;
        }

        int choice = CommonView.promptInt("Select an application to withdraw (or 0 to go back): ", 0, applications.size());
        if (choice > 0) {
            Application selectedApp = applications.get(choice - 1);
            try {
                applicantApplicationService.withdrawApplication(applicant, selectedApp.getApplicationID());
                ApplicantApplicationView.displayWithdrawalSuccess();
            } catch (Exception e) {
                ApplicantApplicationView.displayWithdrawalError(e.getMessage());
            }
        }
        CommonView.prompt("Press Enter to continue...");

    }
}