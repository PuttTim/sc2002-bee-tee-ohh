package views;

import controllers.EnquiryController;

import models.Applicant;
import models.Manager;
import models.Officer;
import models.Project;
import models.Registration;
import models.enums.FlatType;
import repositories.ApplicationRepository;
import repositories.ManagerRepository;
import repositories.ProjectRepository;

import java.util.List;

public class ProjectView {
    public static void showProjectMenu(Applicant applicant, List<Project> projects) {
        List<String> options = List.of(
                "View Project Details",
                "Create New Enquiry"
        );

        while (true) {
            int choice = CommonView.displayMenuWithBacking("Project Menu", options);
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
                    case 0 -> {
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
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }

    public static void displayProjectDetailsOfficerView(Project project) {
        // TODO update this with more details for the officer to see, 
        // maybe how many people applied for each unit type, how many officers are there, 
        // how many officers slots are remaining and who is the manager of the project (name)
        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        Manager manager = ManagerRepository.getByNRIC(project.getManagerNRIC());
        CommonView.displayMessage("Manager: " + (manager != null ? String.format("%s (%s)", manager.getName(), manager.getUserNRIC()) : "N/A"));
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }

    public static void displayProjectDetailsManagerView(Project project) {
        CommonView.displayShortSeparator();

        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        Manager manager = ManagerRepository.getByNRIC(project.getManagerNRIC());
        CommonView.displayMessage("Manager: " + (manager != null ? String.format("%s (%s)", manager.getName(), manager.getUserNRIC()) : "N/A"));
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }

    public static void displayOfficerRegistrations(List<Project> projects, List<Registration> officerRegistrations, Officer officer) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        CommonView.displayHeader("Projects and their Officer Registrations");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            CommonView.displayMessage((i + 1) + ". Project: " + project.getProjectName());

            if (officerRegistrations.stream().anyMatch(r -> r.getProjectID().equals(project.getProjectID()))) {
                officerRegistrations.stream()
                        .filter(r -> r.getProjectID().equals(project.getProjectID()) && r.getOfficer().getUserNRIC().equals(officer.getUserNRIC()))
                        .forEach(r -> CommonView.displayMessage(
                                "   Status: " + r.getRegistrationStatus() + " AS Officer" +
                                        "\n      Registration Date: " + r.getRegistrationDate() +
                                        "\n      Last Updated: " + r.getLastUpdated() +
                                        "\n      Approved By: " + (r.getApprovedBy() != null ? r.getApprovedBy().getName() : "N/A"
                                        )));
                
            } 
            else if (ApplicationRepository.getByProject(project.getProjectID()).stream().anyMatch(a -> a.getApplicantNRIC().equals(officer.getUserNRIC()))) {
                CommonView.displayMessage("   Status: Registered as Applicant");
            } 
            else {
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
        return CommonView.promptYesNo("Make project visible?: ");
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
