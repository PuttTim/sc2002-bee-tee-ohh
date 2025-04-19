package views;

import controllers.EnquiryController;

import models.Applicant;
import models.Project;
import models.enums.FlatType;

import java.util.List;

public class ProjectView {
    public static void showProjectMenu(Applicant applicant, List<Project> projects) {
        List<String> options = List.of(
                "View Project Details",
                "Create New Enquiry",
                "Back to Main Menu"
        );

        while (true) {
            int choice = CommonView.displayMenu("Project Menu", options);
            try {
                switch (choice) {
                    case 1 -> {
                        int projectChoice = getProjectChoice(projects);
                        if (projectChoice != -1) {
                            Project selectedProject = projects.get(projectChoice - 1);
                            displayProjectDetails(selectedProject);
                        }
                    }
                    case 2 -> EnquiryController.createNewEnquiry(applicant);
                    case 3 -> {
                        return;
                    }
                }
            } catch (Exception e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
    }

    public static void displayProjectList(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        CommonView.displayHeader("Available Projects");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            CommonView.displayMessage((i + 1) + ". " + project.getProjectName());
        }
    }

    public static void displayAvailableProjects(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available at the moment.");
            return;
        }

        CommonView.displayHeader("Available Projects");
        for (Project project : projects) {
            displayProjectDetails(project);
            CommonView.displayMessage("----------------------------------------");
        }
    }

    public static void displayProjectDetails(Project project) {
        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i));
        }
    }

    public static void displayOfficerRegistrations(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        CommonView.displayHeader("Projects and their Officer Registrations");
        for (Project project : projects) {
            CommonView.displayMessage("\nProject: " + project.getProjectName());
            List<String> officers = project.getOfficers();
            if (officers.isEmpty()) {
                CommonView.displayMessage("No officers registered");
            } else {
                CommonView.displayMessage("Registered Officers:");
                for (String officerNRIC : officers) {
                    CommonView.displayMessage("- Officer NRIC: " + officerNRIC);
                }
            }
            CommonView.displayMessage("Available slots: " + project.getOfficerSlots());
            CommonView.displayMessage("----------------------------------------");
        }
    }

    public static String getProjectName() {
        return CommonView.prompt("Enter project name: ");
    }

    public static String getProjectLocation() {
        return CommonView.prompt("Enter project location: ");
    }

    public static String getProjectId() {
        return CommonView.prompt("Enter project ID: ");
    }

    public static int getOfficerSlots() {
        return CommonView.promptInt("Enter the number of Officer slots: ");
    }

    public static boolean getProjectVisibility() {
        String input = CommonView.prompt("Make project visible? (yes/no): ").toLowerCase();
        while (!input.equals("yes") && !input.equals("no")) {
            CommonView.displayError("Invalid input! Please enter (yes/no)");
            input = CommonView.prompt("Make project visible? (yes/no): ").toLowerCase();
        }
        return input.equals("yes");
    }

    public static int getProjectChoice(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayError("No projects available.");
            return -1;
        }

        CommonView.displayHeader("Select a project to view details:");
        for (int i = 0; i < projects.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + projects.get(i).getProjectName());
        }
        return CommonView.promptInt("\nEnter your choice: ", 1, projects.size());
    }

    public static void displayProjectCreationSuccess(String projectName) {
        CommonView.displaySuccess("Project \"" + projectName + "\" created successfully.");
    }

    public static void displayProjectUpdateSuccess() {
        CommonView.displaySuccess("Project updated successfully.");
    }

    public static void displayProjectDeleteSuccess() {
        CommonView.displaySuccess("Project deleted successfully.");
    }

    public static void displayProjectNotFound() {
        CommonView.displayError("Project not found!");
    }

    public static void displayVisibilityUpdateSuccess() {
        CommonView.displaySuccess("Project visibility updated successfully.");
    }
}
