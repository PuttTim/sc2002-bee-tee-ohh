package views;

import models.*;
import services.ApplicantApplicationService;
import controllers.ApplicantController;

import java.util.List;
import java.util.Scanner;

public class ApplicantApplicationView {
    private ApplicantApplicationView applicationView;
    private ApplicantApplicationService applicationService;
    private ApplicantController applicantController;
    private Scanner scanner;

    //constructor
    public ApplicantApplicationView(ApplicantApplicationView applicationView,
                                    ApplicantApplicationService service,
                                    ApplicantController applicantController) {
        this.applicationView = applicationView;
        this.applicationService = service;
        this.applicantController = applicantController;
        this.scanner = new Scanner(System.in);
    }

    public void promptApplication(Applicant applicant, List<Project> allProjects) {
        //get list of eligible projects
        List<Project> eligibleProjects = applicationService.viewEligibleProjects(applicant, allProjects);

        if (eligibleProjects.isEmpty()) { //no eligible projects for the applicant
            System.out.println("No eligible projects available.");
            return;
        }

        for (int i = 0; i < eligibleProjects.size(); i++) {
            Project project = eligibleProjects.get(i);
            System.out.printf("%d. %s (%s)\n", i + 1, project.getProjectName(), project.getLocation());
            //display the eligible projects (name and location) for the applicant
        }

        System.out.print("Select a project number: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1; //user input

        if (choice < 0 || choice >= eligibleProjects.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Project selected = eligibleProjects.get(choice);
        System.out.print("Enter flat type (e.g., TWO_ROOM, THREE_ROOM): "); // Updated prompt for clarity
        String flatTypeInput = scanner.nextLine().trim(); //user input

        try {
            //initialise new application from applicant
            // Assuming applicationService is instantiated correctly
            Application application = applicationService.applyForProject(applicant, selected, flatTypeInput);
            System.out.println("Application submitted successfully.");
            System.out.println("Application ID: " + application.getApplicationID()); // Corrected getter
            System.out.println("Project: " + selected.getProjectName());
            System.out.println("Flat Type: " + application.getSelectedFlatType());

        } catch (IllegalArgumentException e) {
            System.out.println("Error submitting application: " + e.getMessage()); //error message
        } catch (Exception e) {
            // Catch other potential exceptions during application process
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void viewApplicationStatus(Application application) {
        if (application == null) { //no application
            System.out.println("No application found.");
            return;
        }

        //else (if application exists)
        //display the id, project name and status
        System.out.println("Application ID: " + application.getApplicationID());
        System.out.println("Project: " + application.getProject().getProjectName());
        System.out.println("Status: " + application.getApplicationStatus());
    }

    public void handleWithdraw(Application application) {
        if (application == null) { //no application
            System.out.println("No application found.");
            return;
        }

        //else call withdrawApplication from services
        applicationService.withdrawApplication(application);
        System.out.println("Withdrawal request submitted.");
    }

    //the menu for application options
    public void showApplicationMenu(Applicant applicant,
                                    List<Project> allProjects,
                                    ApplicantController applicantController) {
        Application application = null;
        while (true) {
            System.out.println("\n===== Application Menu =====");
            System.out.println("1. Apply for a project");
            System.out.println("2. View Application Status");
            System.out.println("3. Withdraw Application");
            System.out.println("4. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    applicationView.promptApplication(applicant, allProjects);
                    break;
                case 2:
                    //fetch application first
                    application = applicationService.getApplicationByApplicant(applicant);
                    //no application = display error message
                    if (application == null) {
                        System.out.println("No application found.");
                    } else {
                        //show application status, use ApplicantApplicationView method
                        applicationView.viewApplicationStatus(application);
                    }
                case 3:
                    //fetch application first
                    application = applicationService.getApplicationByApplicant(applicant);
                    if (application == null) { //check conditions
                        System.out.println("No application found to withdraw.");
                    } else {
                        applicationView.handleWithdraw(application);
                    }
                    break;
                case 4:
                    applicantController.start(applicant, allProjects);
                    return; //route back to ApplicantController.java
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
