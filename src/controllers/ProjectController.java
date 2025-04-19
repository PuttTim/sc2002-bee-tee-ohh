package controllers;

import models.Applicant;
import models.Project;
import models.Officer;
import services.ProjectService;
import views.ProjectView;
import views.AuthView;
import views.CommonView;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller class to handle project-related actions.
 * <p>Project-related actions include:</p>
 * <ul>
 *     <li>Displaying visible/all projects</li>
 *     <li>Creating, editing, deleting projects</li>
 *     <li>Toggling project visibility</li>
 *     <li>Managing officer registrations</li>
 *     <li>Viewing project-related enquiries</li>
 * </ul>
 */
public class ProjectController {
    /**
     * Displays the list of visible projects to the applicant.
     *
     * @param applicant the applicant who is viewing the projects.
     */
    public static void viewAvailableProjects(Applicant applicant) {
        List<Project> projects = ProjectService.getVisibleProjects();
        CommonView.displayHeader("Available Projects");
        ProjectView.displayAvailableProjects(projects);
        ProjectView.showProjectMenu(applicant, projects);
    }

    /**
     * Allows a manager to create a new project. Manager to enter relevant project details.
     * <ul>
     *   <li>Prompts the user for project name, location, dates, slots</li>
     *   <li>Handles the creation logic through service layer</li>
     * </ul>
     */
    public static void createProject() {
        String projectId = ProjectView.getProjectId();
        String managerNRIC = AuthView.getNRIC();
        String projectName = ProjectView.getProjectName();
        String location = ProjectView.getProjectLocation();
        
        try {
            LocalDateTime startDate = CommonView.promptDate("Enter start date (dd/MM/yyyy): ");
            LocalDateTime endDate = CommonView.promptDate("Enter end date (dd/MM/yyyy): ");
                        
            int officerSlots = ProjectView.getOfficerSlots();
            boolean visibility = ProjectView.getProjectVisibility();

            ProjectService.createProject(projectId, managerNRIC, projectName, location, 
                startDate, endDate, officerSlots, visibility);
            ProjectView.displayProjectCreationSuccess(projectName);
        } catch (Exception e) {
            CommonView.displayError("Error creating project: " + e.getMessage());
        }
    }

    /**
     * Allows a manager to update a project's location and timeline.
     * <ul>
     *   <li>Finds a project by name</li>
     *   <li>Updates project's details using user input</li>
     * </ul>
     */
    public static void editProject() {
        String projectName = ProjectView.getProjectName();
        Project project = ProjectService.getProjectByName(projectName);
        
        if (project == null) {
            ProjectView.displayProjectNotFound();
            return;
        }

        String location = ProjectView.getProjectLocation();
        try {
            LocalDateTime startDate = CommonView.promptDate("Enter start date (dd/MM/yyyy): ");
            LocalDateTime endDate = CommonView.promptDate("Enter end date (dd/MM/yyyy): ");

            ProjectService.updateProject(project, location, startDate, endDate);
            ProjectView.displayProjectUpdateSuccess();
        } catch (Exception e) {
            CommonView.displayError("Error updating project: " + e.getMessage());
        }
    }

    /**
     * Deletes a project based on project name entered by the user.
     */
    public static void deleteProject() {
        String projectName = ProjectView.getProjectName();
        ProjectService.deleteProject(projectName);
        ProjectView.displayProjectDeleteSuccess();
    }

    /**
     * Changes the visibility of a project.
     */
    public static void toggleProjectVisibility() {
        String projectName = ProjectView.getProjectName();
        boolean visibility = ProjectView.getProjectVisibility();
        ProjectService.toggleProjectVisibility(projectName, visibility);
        ProjectView.displayVisibilityUpdateSuccess();
    }

    /**
     * Displays all projects, both visible and hidden ones.
     */
    public static void viewAllProjects() {
        List<Project> projects = ProjectService.getAllProjects();
        CommonView.displayHeader("Available Projects");
        ProjectView.displayAvailableProjects(projects);
    }

    /**
     * Displays all enquiries made for a specific project.
     *
     * @param projectName the name of the project which enquiries are being viewed.
     */
    public static void viewProjectEnquiries(String projectName) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project != null) {
            EnquiryController.viewProjectEnquiries(project);
        } else {
            ProjectView.displayProjectNotFound();
        }
    }

    /**
     * Displays the list of officer registrations for all projects.
     * Placeholder.
     */
    public static void viewOfficerRegistrations() {
        List<Project> projects = ProjectService.getAllProjects();
        // ProjectView.displayOfficerRegistrations(projects);
    }

    /**
     * If there are available officer slots, registers an officer to handle a project.
     *
     * @param officer the officer to register for a project.
     * @param projectName the name of the project to assign the officer to.
     */
    public static void handleOfficerRegistration(Officer officer, String projectName) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project == null) {
            ProjectView.displayProjectNotFound();
            return;
        }
        
        if (!ProjectService.hasOfficerSlots(project)) {
            CommonView.displayError("Project has no more officer slots.");
            return;
        }

        ProjectService.addOfficerToProject(project, officer.getUserNRIC());
        CommonView.displaySuccess("Officer registration submitted successfully.");
    }

    /**
     * Removes a registered officer from a specific project.
     *
     * @param projectName the name of the specific project.
     * @param officerNRIC the NRIC of the officer to be removed.
     */
    public static void removeOfficerFromProject(String projectName, String officerNRIC) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project != null) {
            ProjectService.removeOfficerFromProject(project, officerNRIC);
        } else {
            ProjectView.displayProjectNotFound();
        }
    }
}
