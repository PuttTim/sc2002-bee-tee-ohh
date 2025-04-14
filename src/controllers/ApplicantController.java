package controllers;

import models.*;
import models.enums.FlatType;
import repositories.ProjectRepository;
import views.*;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                        List<Project> allProjects = ProjectRepository.getAll();
                        ApplicantApplicationView.showApplicationMenu(applicant, allProjects);
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

    public static void viewAvailableProjects() {
        List<Project> projects = ProjectRepository.getAll().stream()
                .filter(Project::isVisible)
                .collect(Collectors.toList());
                
        if (projects.isEmpty()) {
            System.out.println("No projects available at the moment.");
            return;
        }

        System.out.println("\n===== Available Projects =====");
        for (Project project : projects) {
            System.out.println("\nProject Name: " + project.getProjectName());
            System.out.println("Location: " + project.getLocation());
            System.out.println("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
            System.out.println("Available Flat Types:");
            List<FlatType> flatTypes = project.getFlatTypes();
            for (int i = 0; i < flatTypes.size(); i++) {
                System.out.println((i + 1) + ". " + flatTypes.get(i));
            }
        }
    }

    public static void submitApplication(Applicant applicant) {
        List<Project> allProjects = ProjectRepository.getAll();
        ApplicantApplicationView.promptApplication(applicant, allProjects);
    }

    public static void viewMyApplications(Applicant applicant) {
        ApplicantApplicationView.displayApplicationStatus(applicant);
    }
}
