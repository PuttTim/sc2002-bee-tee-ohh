package controllers;

import models.*;
import models.enums.MaritalStatus;
import models.enums.Role;
import repositories.*;
import views.EnquiryView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ManagerController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void createProject() {
        System.out.println("Enter the ID of the Project:");
        String projectId = scanner.nextLine();
        System.out.println("Enter the NRIC of the Project manager:");
        String nric = scanner.nextLine();
        System.out.println("Enter the name of the Project manager:");
        String managerName = scanner.nextLine();
        System.out.println("Enter the age of Project manager:");
        int managerAge = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        System.out.println("Enter Project manager marital status (0 = SINGLE / 1 = MARRIED):");
        MaritalStatus managerMS = MaritalStatus.SINGLE;
        while (true) {
            try {
                int inputMS = Integer.parseInt(scanner.nextLine());
                if (inputMS == 0) {
                    managerMS = MaritalStatus.SINGLE;
                    break;
                } else if (inputMS == 1) {
                    managerMS = MaritalStatus.MARRIED;
                    break;
                } else {
                    System.out.println("Invalid input, enter (0 = SINGLE / 1 = MARRIED):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number (0 or 1):");
            }
        }
        
        System.out.println("Enter the password of the Project manager:");
        String password = scanner.nextLine();
        Manager manager = new Manager(managerName, managerName, password, managerAge, projectId);
        
        System.out.println("Enter the name of the Project:");
        String projectName = scanner.nextLine();
        System.out.println("Enter the Project location:");
        String location = scanner.nextLine();
        
        LocalDate startDate;
        while (true) {
            System.out.print("Enter the Project start date in (dd/mm/yyyy):");
            String startDateInput = scanner.nextLine().trim();
            
            if (!startDateInput.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Invalid format! Please use dd/mm/yyyy (e.g 25/12/2023)");
                continue;
            }
            try {
                startDate = LocalDate.parse(startDateInput, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Please enter a valid date (e.g 25/12/2023)");
            }
        }
        
        LocalDate endDate;
        while (true) {
            System.out.print("Enter the Project end date in (dd/mm/yyyy):");
            String endDateInput = scanner.nextLine().trim();
            
            if (!endDateInput.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Invalid format! Please use dd/mm/yyyy (e.g 25/12/2023)");
                continue;
            }
            try {
                endDate = LocalDate.parse(endDateInput, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Please enter a valid date (e.g 25/12/2023)");
            }
        }
        
        System.out.println("Enter the number of Officer slots:");
        int officerSlots = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Is the Project visible to applicants? (yes/no):");
        boolean visibility = false;
        while (true) {
            String visInput = scanner.nextLine().toLowerCase();
            if (visInput.equals("yes")) {
                visibility = true;
                break;
            } else if (visInput.equals("no")) {
                visibility = false;
                break;
            } else {
                System.out.println("Invalid input! Please enter (yes/no):");
            }
        }

        Project newProject = new Project(projectId, nric, projectName, location,
                startDate.atStartOfDay(), endDate.atStartOfDay(),
                officerSlots, visibility);
                
        ProjectRepository.add(newProject);
        ProjectRepository.saveAll();
        System.out.println("Project \"" + projectName + "\" created successfully.");
    }

    public static void editProject(String projectName, String location, String applicationStart, String applicationEnd) {
        Project project = ProjectRepository.getByName(projectName);
        if (project == null) {
            System.out.println("Project not found!");
            return;
        }
        
        try {
            LocalDate startDate = LocalDate.parse(applicationStart, formatter);
            LocalDate endDate = LocalDate.parse(applicationEnd, formatter);
            
            project.setLocation(location);
            project.setApplicationStartDate(startDate.atStartOfDay());
            project.setApplicationEndDate(endDate.atStartOfDay());
            
            ProjectRepository.saveAll();
            System.out.println("Project updated successfully.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Please use dd/MM/yyyy");
        }
    }

    public static void deleteProject(String projectName) {
        Project project = ProjectRepository.getByName(projectName);
        if (project != null) {
            ProjectRepository.remove(project);
            ProjectRepository.saveAll();
            System.out.println("Project deleted successfully.");
        } else {
            System.out.println("Project not found!");
        }
    }

    public static void toggleVisibility() {
        System.out.println("Enter Project name:");
        String projectName = scanner.nextLine();
        
        Project project = ProjectRepository.getByName(projectName);
        if (project != null) {
            System.out.println("Make Project visible? (yes/no):");
            String input = scanner.nextLine().toLowerCase();
            
            if (input.equals("yes") || input.equals("no")) {
                project.setVisible(input.equals("yes"));
                ProjectRepository.saveAll();
                System.out.println("Project visibility updated successfully.");
            } else {
                System.out.println("Invalid input! Please enter (yes/no)");
            }
        } else {
            System.out.println("Project not found!");
        }
    }

    public static void viewAllProjects() {
        List<Project> projects = ProjectRepository.getAll();
        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }
        
        System.out.println("\nAll Projects:");
        System.out.println("----------------------------------------");
        for (Project project : projects) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Name: " + project.getProjectName());
            System.out.println("Location: " + project.getLocation());
            System.out.println("Application Open: " + project.getApplicationOpenDate());
            System.out.println("Application Close: " + project.getApplicationCloseDate());
            System.out.println("Officer Slots: " + project.getOfficerSlots());
            System.out.println("Visible: " + (project.isVisible() ? "Yes" : "No"));
            System.out.println("----------------------------------------");
        }
    }

    public static void viewProjectEnquiries(Project project) {
        // EnquiryController.manageProjectEnquiries();
    }

    public static void viewOfficerRegistrations() {
        List<Project> projects = ProjectRepository.getAll();
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }

        System.out.println("\nProjects and their Officer Registrations:");
        System.out.println("----------------------------------------");
        
        for (Project project : projects) {
            System.out.println("Project: " + project.getProjectName());
            List<String> officers = project.getOfficers();
            if (officers.isEmpty()) {
                System.out.println("No officers registered");
            } else {
                System.out.println("Registered Officers:");
                for (String officerNRIC : officers) {
                    System.out.println("- Officer NRIC: " + officerNRIC);
                }
            }
            System.out.println("Available slots: " + project.getOfficerSlots());
            System.out.println("----------------------------------------");
        }
    }

    public static void approveOfficerRegistration(Project project, boolean approve) {
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        System.out.print("Enter officer NRIC: ");
        String officerNRIC = scanner.nextLine().trim();
        
        if (approve) {
            if (project.getOfficerSlots() <= 0) {
                System.out.println("No available slots for this project.");
                return;
            }
            
            if (!project.getOfficers().contains(officerNRIC)) {
                project.addOfficer(officerNRIC);
                project.reduceOfficerSlot();
                ProjectRepository.saveAll();
                System.out.println("Officer registration approved successfully.");
            } else {
                System.out.println("Officer is already registered for this project.");
            }
        } else {
            if (project.getOfficers().contains(officerNRIC)) {
                project.removeOfficer(officerNRIC);
                ProjectRepository.saveAll();
                System.out.println("Officer registration rejected/removed successfully.");
            } else {
                System.out.println("Officer is not registered for this project.");
            }
        }
    }
}
