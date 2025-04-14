package controllers;

import models.*;
import models.enums.FlatType;
import repositories.ProjectRepository;
import services.*;
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
                        showEnquiryMenu(applicant);
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

    private static void showEnquiryMenu(Applicant applicant) {
        while (true) {
            try {
                System.out.println("\n===== Enquiry Menu =====");
                System.out.println("1. View All Enquiries");
                System.out.println("2. Submit New Enquiry");
                System.out.println("3. Edit Enquiry");
                System.out.println("4. Delete Enquiry");
                System.out.println("5. Back to Main Menu");
                System.out.print("Select an option: ");

                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1: // View All Enquiries
                        List<Enquiry> enquiries = ApplicantEnquiryService.viewEnquiriesByApplicant(applicant);
                        ApplicantEnquiryView.displayEnquiries(enquiries);
                        break;

                    case 2: // Submit New Enquiry
                        List<Project> availableProjects = ProjectRepository.getAll().stream()
                                .filter(Project::isVisible)
                                .collect(Collectors.toList());
                        if (availableProjects.isEmpty()) {
                            ApplicantEnquiryView.showError("No projects available for enquiry");
                            break;
                        }
                        Enquiry newEnquiry = ApplicantEnquiryView.getEnquiryInput(applicant.getUserNRIC(), availableProjects);
                        if (newEnquiry != null) {
                            ApplicantEnquiryService.submitEnquiry(newEnquiry);
                            ApplicantEnquiryView.showSuccess("Enquiry submitted successfully");
                        }
                        break;

                    case 3: // Edit Enquiry
                        List<Enquiry> existingEnquiries = ApplicantEnquiryService.viewEnquiriesByApplicant(applicant);
                        if (existingEnquiries.isEmpty()) {
                            ApplicantEnquiryView.showError("No enquiries available to edit");
                            break;
                        }
                        ApplicantEnquiryView.displayEnquiries(existingEnquiries);
                        int editIndex = ApplicantEnquiryView.getEnquiryToEditOrDelete("edit", existingEnquiries.size());
                        if (editIndex >= 0 && editIndex < existingEnquiries.size()) {
                            String newContent = ApplicantEnquiryView.getUpdatedContents();
                            if (newContent != null) {
                                ApplicantEnquiryService.editEnquiry(applicant, existingEnquiries.get(editIndex).getEnquiryID(), newContent);
                                ApplicantEnquiryView.showSuccess("Enquiry updated successfully");
                            }
                        } else {
                            ApplicantEnquiryView.showError("Invalid selection");
                        }
                        break;

                    case 4: // Delete Enquiry
                        List<Enquiry> enquiriesToDelete = ApplicantEnquiryService.viewEnquiriesByApplicant(applicant);
                        if (enquiriesToDelete.isEmpty()) {
                            ApplicantEnquiryView.showError("No enquiries available to delete");
                            break;
                        }
                        ApplicantEnquiryView.displayEnquiries(enquiriesToDelete);
                        int deleteIndex = ApplicantEnquiryView.getEnquiryToEditOrDelete("delete", enquiriesToDelete.size());
                        if (deleteIndex >= 0 && deleteIndex < enquiriesToDelete.size()) {
                            ApplicantEnquiryService.deleteEnquiry(applicant, enquiriesToDelete.get(deleteIndex).getEnquiryID());
                            ApplicantEnquiryView.showSuccess("Enquiry deleted successfully");
                        } else {
                            ApplicantEnquiryView.showError("Invalid selection");
                        }
                        break;

                    case 5: // Back to Main Menu
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
            System.out.println("-".repeat(30));
        }
    }

    public static void submitApplication(Applicant applicant) {
        List<Project> allProjects = ProjectRepository.getAll();
        ApplicantApplicationView.promptApplication(applicant, allProjects);
    }

    public static void viewMyApplications(Applicant applicant) {
        ApplicantApplicationView.displayApplicationStatus(applicant);
    }

    public static void submitEnquiry(Applicant applicant) {
        List<Project> availableProjects = ProjectRepository.getAll().stream()
                .filter(Project::isVisible)
                .collect(Collectors.toList());
                
        if (availableProjects.isEmpty()) {
            System.out.println("No projects available for enquiry.");
            return;
        }

        Enquiry enquiry = ApplicantEnquiryView.getEnquiryInput(applicant.getUserNRIC(), availableProjects);
        if (enquiry != null) {
            ApplicantEnquiryService.submitEnquiry(enquiry);
            System.out.println("Enquiry submitted successfully.");
        }
    }

    public static void viewMyEnquiries(Applicant applicant) {
        List<Enquiry> enquiries = ApplicantEnquiryService.viewEnquiriesByApplicant(applicant);
        ApplicantEnquiryView.displayEnquiries(enquiries);
    }
}
