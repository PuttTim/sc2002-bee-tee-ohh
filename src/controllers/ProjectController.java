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

}
