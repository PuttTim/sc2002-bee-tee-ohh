package views;

import controllers.EnquiryController;
import models.Applicant;
import models.Officer;
import models.Project;
import models.Registration;
import models.enums.FlatType;
import repositories.ApplicationRepository;
import repositories.ProjectRepository;

import java.util.List;

/**
 * A view class for managing project-related operations
 * and displaying project information to applicants and officers.
 */
public class ProjectView {

    /**
     * Displays a menu with project-related options for an applicant.
     *
     * @param applicant the applicant interacting with the system.
     * @param projects the list of available projects.
     */
    public static void showProjectMenu(Applicant applicant, List<Project> projects) {
        List<String> options = List.of(
                "View Project Details",
                "Create New Enquiry"
        );

        //continuously show the menu until the user exits
        while (true) {
            int choice = CommonView.displayMenuWithBacking("Project Menu", options);
            try {
                switch (choice) {
                    case 1 -> {
                        // Allow the applicant to choose a project and view its details
                        int projectChoice = getProjectChoice(projects);
                        if (projectChoice != -1) {
                            Project selectedProject = projects.get(projectChoice - 1);
                            displayProjectDetails(selectedProject);
                        }
                    }
                    case 2 -> EnquiryController.createNewEnquiry(applicant); // Allow the applicant to create a new enquiry
                    case 0 -> { return; } // Exit the menu
                }
            } catch (Exception e) {
                CommonView.displayError("Please enter a valid number!"); // Handle invalid input
            }
        }
    }

    /**
     * Displays a list of available projects.
     *
     * @param projects the list of available projects.
     */
    public static void displayProjectList(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        //display each project in the list
        CommonView.displayHeader("Available Projects");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            CommonView.displayMessage((i + 1) + ". " + project.getProjectName());
        }
    }

    /**
     * Displays the details of all available projects.
     *
     * @param projects the of available projects.
     */
    public static void displayAvailableProjects(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available at the moment.");
            return;
        }

        // Display details of each project
        for (Project project : projects) {
            displayProjectDetails(project);
            CommonView.displayMessage("----------------------------------------");
        }
    }

    /**
     * Displays detailed information about a specific project.
     *
     * @param project the project to display.
     */
    public static void displayProjectDetails(Project project) {
        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");

        // Display details about each flat type
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }

    /**
     * Displays project details from the officer's perspective.
     *
     * @param project the project to display.
     */
    public static void displayProjectDetailsOfficerView(Project project) {
        // Display detailed information for officers
        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");

        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }

    /**
     * Displays the registrations of officers for the selected projects.
     *
     * @param projects a list of projects.
     * @param officerRegistrations a list of officer registrations.
     * @param officer the officer whose registration information is to be displayed.
     */
    public static void displayOfficerRegistrations(List<Project> projects, List<Registration> officerRegistrations, Officer officer) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        // Display officer registration information for each project
        CommonView.displayHeader("Projects and their Officer Registrations");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            CommonView.displayMessage((i + 1) + ". Project: " + project.getProjectName());

            // Display officer registration details
            if (officerRegistrations.stream().anyMatch(r -> r.getProjectID().equals(project.getProjectID()))) {
                officerRegistrations.stream()
                        .filter(r -> r.getProjectID().equals(project.getProjectID()) && r.getOfficer().getUserNRIC().equals(officer.getUserNRIC()))
                        .forEach(r -> CommonView.displayMessage(
                                "   Status: " + r.getRegistrationStatus() + " AS Officer" +
                                        "\n      Registration Date: " + r.getRegistrationDate() +
                                        "\n      Last Updated: " + r.getLastUpdated() +
                                        "\n      Approved By: " + (r.getApprovedBy() != null ? r.getApprovedBy().getName() : "N/A"
                                )));

            } else if (ApplicationRepository.getByProject(project.getProjectID()).stream().anyMatch(a -> a.getApplicantNRIC().equals(officer.getUserNRIC()))) {
                CommonView.displayMessage("   Status: Registered as Applicant");
            } else {
                CommonView.displayMessage("   Status: Not Registered");
            }
            CommonView.displayMessage("   Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
            List<String> officersNames = ProjectRepository.getProjectOfficerNames(project.getProjectID());
            CommonView.displayMessage("   Available Officer Slots: " + project.getOfficerSlots());

            if (officersNames.isEmpty()) {
                CommonView.displayMessage("   No officers registered");
            } else {
                CommonView.displayMessage("   Registered Officers:");
                for (String name : officersNames) {
                    CommonView.displayMessage("     - " + name);
                }
            }
            CommonView.displayMessage("----------------------------------------");
        }
    }

    /**
     * Prompts the user to enter a project name.
     *
     * @return the project name entered by the user.
     */
    public static String getProjectName() {
        return CommonView.prompt("Enter project name: ");
    }

    /**
     * Prompts the user to enter a project location.
     *
     * @return the project location entered by the user.
     */
    public static String getProjectLocation() {
        return CommonView.prompt("Enter project location: ");
    }

    /**
     * Prompts the user to enter a project ID.
     *
     * @return the project ID entered by the user.
     */
    public static String getProjectId() {
        return CommonView.prompt("Enter project ID: ");
    }

    /**
     * Prompts the user to enter the number of officer slots for a project.
     *
     * @return the number of officer slots entered by the user.
     */
    public static int getOfficerSlots() {
        return CommonView.promptInt("Enter the number of Officer slots: ");
    }

    /**
     * Prompts the user to specify whether the project should be visible.
     *
     * @return <code>true</code> if the project should be visible, <code>false</code> otherwise.
     */
    public static boolean getProjectVisibility() {
        String input = CommonView.prompt("Make project visible? (yes/no): ").toLowerCase();
        while (!input.equals("yes") && !input.equals("no")) {
            CommonView.displayError("Invalid input! Please enter (yes/no)");
            input = CommonView.prompt("Make project visible? (yes/no): ").toLowerCase();
        }
        return input.equals("yes");
    }

    /**
     * Prompts the user to select a project from the available list.
     *
     * @param projects the list of available projects.
     * @return the selected project index, or -1 if no selection was made.
     */
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

    /**
     * Displays a success message when a project is successfully created.
     *
     * @param projectName the name of the project that was created.
     */
    public static void displayProjectCreationSuccess(String projectName) {
        CommonView.displaySuccess("Project \"" + projectName + "\" created successfully.");
    }

    /**
     * Displays a success message when a project is successfully updated.
     */
    public static void displayProjectUpdateSuccess() {
        CommonView.displaySuccess("Project updated successfully.");
    }

    /**
     * Displays a success message when a project is successfully deleted.
     */
    public static void displayProjectDeleteSuccess() {
        CommonView.displaySuccess("Project deleted successfully.");
    }

    /**
     * Displays an error message when a project is not found.
     */
    public static void displayProjectNotFound() {
        CommonView.displayError("Project not found!");
    }

    /**
     * Displays a success message when a project's visibility is updated.
     */
    public static void displayVisibilityUpdateSuccess() {
        CommonView.displaySuccess("Project visibility updated successfully.");
    }
}
