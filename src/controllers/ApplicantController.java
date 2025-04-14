package controllers;

import models.*;
import repositories.ProjectRepository;
import views.*;
import java.util.Scanner;

public class ApplicantController {
    private static final Scanner scanner = new Scanner(System.in);

    public static void start(Applicant applicant) {
        while (true) {
            try {
                System.out.println("\n===== Applicant Main Menu =====");
                System.out.println("Welcome, " + applicant.getName());
                if (applicant.hasBookedFlat()) {
                    System.out.println("You have a booked flat in project: " + applicant.getBookedProjectId());
                }
                System.out.println("\n1. Application Services");
                System.out.println("2. Enquiry Services");
                System.out.println("3. Logout");
                System.out.print("Select an option: ");

                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        ProjectController.viewAvailableProjects();
                        break;
                    case 2:
                        EnquiryController.showEnquiryMenu(applicant);
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    public static void newApplication(Applicant applicant) {
        ApplicantApplicationView.promptApplication(applicant, ProjectRepository.getAll());
    }

    public static void viewMyApplications(Applicant applicant) {
        ApplicantApplicationView.displayApplicationStatus(applicant);
    }
}
