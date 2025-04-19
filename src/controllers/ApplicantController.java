package controllers;

import java.util.List;

import models.Applicant;
import models.Project;

import repositories.ProjectRepository;
import services.ProjectService;
import views.ApplicantApplicationView;

public class ApplicantController {

    public static void newApplication(Applicant applicant) {
        ApplicantApplicationView.promptApplication(applicant, ProjectService.getVisibleProjects());
    }

    public static void viewMyApplications(Applicant applicant) {
        List<Project> projects = ProjectService.getVisibleProjects();
        ApplicantApplicationView.displayApplicationStatus(applicant);
        ApplicantApplicationView.showApplicationMenu(applicant, projects);
    }
}