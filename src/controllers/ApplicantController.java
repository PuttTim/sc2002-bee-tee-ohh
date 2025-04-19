package controllers;

import models.*;
import repositories.ProjectRepository;
import services.ProjectService;
import views.*;
import java.util.List;

/**
 * Controller class that is responsible for handling applicant-related actions.
 * <p>Applicant-related actions include:</p>
 * <ul>
 *     <li>Making new applications</li>
 *     <li>Viewing existing application statuses</li>
 * </ul>
 */
public class ApplicantController {

    /**
     * Starts a new application for the applicant.
     *
     * @param applicant the applicant who is applying for a BTO project.
     */
    public static void newApplication(Applicant applicant) {
        ApplicantApplicationView.promptApplication(applicant, ProjectService.getVisibleProjects());
    }

    /**
     * Allows the applicant to view current application statuses and application menu options.
     *
     * @param applicant the applicant who has applied for a BTO project.
     */
    public static void viewMyApplications(Applicant applicant) {
        List<Project> projects = ProjectService.getVisibleProjects();
        ApplicantApplicationView.displayApplicationStatus(applicant);
        ApplicantApplicationView.showApplicationMenu(applicant, projects);
    }
}