package controllers;

import java.time.LocalDateTime;
import java.util.List;

import models.Applicant;
import models.Enquiry;
import models.Officer;
import models.Project;
import services.EnquiryService;
import services.ProjectService;

import views.ProjectView;
import views.AuthView;
import views.CommonView;
import views.EnquiryView;

/**
 * <p>Handles all operations related to project management in the housing project system.</p>
 * <ul>
 *   <li>Views available projects for applicants.</li>
 *   <li>Creates, edits, deletes, and manages project details.</li>
 *   <li>Toggles project visibility.</li>
 *   <li>Handles officer registrations for projects.</li>
 *   <li>Views and manages project enquiries.</li>
 * </ul>
 */
public class ProjectController {

    /**
     * <p>Displays the list of available projects for an applicant to view.</p>
     * <ul>
     *   <li>Shows a list of projects the applicant can apply for.</li>
     *   <li>Allows the applicant to select a project for more details.</li>
     * </ul>
     * @param applicant The applicant viewing the available projects.
     */
    public static void viewAvailableProjects(Applicant applicant) {
        List<Project> projects = ProjectService.getVisibleProjects();
        CommonView.displayHeader("Available Projects");
        ProjectView.displayAvailableProjects(projects);
        ProjectView.showProjectMenu(applicant, projects);
    }

    /**
     * <p>Creates a new project in the system.</p>
     * <ul>
     *   <li>Prompts for project details such as ID, name, location, dates, and officer slots.</li>
     *   <li>Attempts to create the project and display a success message.</li>
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
     * <p>Edits the details of an existing project.</p>
     * <ul>
     *   <li>Prompts for new project details (location, start/end dates).</li>
     *   <li>Updates the project if found and displays a success message.</li>
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
     * <p>Deletes an existing project from the system.</p>
     * <ul>
     *   <li>Prompts for the project name and attempts to delete it.</li>
     *   <li>Displays a success message upon deletion.</li>
     * </ul>
     */
    public static void deleteProject() {
        String projectName = ProjectView.getProjectName();
        ProjectService.deleteProject(projectName);
        ProjectView.displayProjectDeleteSuccess();
    }

    /**
     * <p>Toggles the visibility status of a project.</p>
     * <ul>
     *   <li>Prompts for the project name and desired visibility status.</li>
     *   <li>Updates the visibility of the project and displays a success message.</li>
     * </ul>
     */
    public static void toggleProjectVisibility() {
        String projectName = ProjectView.getProjectName();
        boolean visibility = ProjectView.getProjectVisibility();
        ProjectService.toggleProjectVisibility(projectName, visibility);
        ProjectView.displayVisibilityUpdateSuccess();
    }

    /**
     * <p>Views all projects in the system.</p>
     * <ul>
     *   <li>Displays a list of all projects, including their details.</li>
     * </ul>
     */
    public static void viewAllProjects() {
        List<Project> projects = ProjectService.getAllProjects();
        CommonView.displayHeader("Available Projects");
        ProjectView.displayAvailableProjects(projects);
    }

    /**
     * <p>Views the enquiries for a specific project.</p>
     * <ul>
     *   <li>Displays all enquiries related to the selected project.</li>
     *   <li>Shows a message if there are no enquiries for the project.</li>
     * </ul>
     * @param project The project whose enquiries are being viewed.
     */
    public static void viewProjectEnquiries(Project project) {
        List<Enquiry> enquiries = EnquiryService.getProjectEnquiries(project);
        if (enquiries.isEmpty()) {
            CommonView.displayMessage("No enquiries for this project.");
        } else {
            EnquiryView.displayEnquiryList(enquiries);
        }
    }

    /**
     * <p>Views the list of officer registrations for all projects.</p>
     * <ul>
     *   <li>Displays a list of all projects and their corresponding officer registrations.</li>
     * </ul>
     */
    public static void viewOfficerRegistrations() {
        List<Project> projects = ProjectService.getAllProjects();
        // ProjectView.displayOfficerRegistrations(projects);
    }

    /**
     * <p>Handles officer registrations for a project.</p>
     * <ul>
     *   <li>Checks if the project exists and has officer slots available.</li>
     *   <li>Registers an officer to the project if possible.</li>
     * </ul>
     * @param officer The officer attempting to register.
     * @param projectName The name of the project the officer is registering for.
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
     * <p>Removes an officer from a project.</p>
     * <ul>
     *   <li>Prompts for the project name and officer NRIC.</li>
     *   <li>Removes the officer from the project if found.</li>
     * </ul>
     * @param projectName The name of the project.
     * @param officerNRIC The NRIC of the officer to be removed.
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
