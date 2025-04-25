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
 *   <li>Handles officer registrations for projects.</li>
 *   <li>Views and manages project enquiries.</li>
 * </ul>
 */
public class ProjectController {

    private final ProjectService projectService;
    private final EnquiryService enquiryService;

    /**
     * Initializes a new instance of the ProjectController class.
     *
     * This constructor sets up the services required for managing projects and handling enquiries.
     * It initializes the ProjectService and EnquiryService as instances for use within the controller.
     */
    public ProjectController() {
        this.projectService = ProjectService.getInstance();
        this.enquiryService = EnquiryService.getInstance();
    }


    /**
     * <p>Displays the list of available projects for an applicant to view.</p>
     * <ul>
     *   <li>Shows a list of projects the applicant can apply for.</li>
     *   <li>Allows the applicant to select a project for more details.</li>
     * </ul>
     * @param applicant The applicant viewing the available projects.
     */
    public void viewAvailableProjects(Applicant applicant) {
        List<Project> projects = projectService.getVisibleProjects();
        CommonView.displayHeader("Available Projects");
        ProjectView.displayAvailableProjects(projects);
        ProjectView.showProjectMenu(applicant, projects);
    }

    /**
     * <p>Views all projects in the system.</p>
     * <ul>
     *   <li>Displays a list of all projects, including their details.</li>
     * </ul>
     */
    public void viewAllProjects() {
        List<Project> projects = projectService.getAllProjects();
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
    public void viewProjectEnquiries(Project project) {
        List<Enquiry> enquiries = enquiryService.getProjectEnquiries(project);
        if (enquiries.isEmpty()) {
            CommonView.displayMessage("No enquiries for this project.");
        } else {
            EnquiryView.displayEnquiryList(enquiries);
        }
    }

}
