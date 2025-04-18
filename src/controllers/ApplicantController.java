package controllers;

import models.*;
import repositories.ProjectRepository;
import services.ProjectService;
import views.*;
import java.util.List;

public class ApplicantController {

    public static void newApplication(Applicant applicant) {
        ApplicantApplicationView.promptApplication(applicant, ProjectRepository.getAll());
    }

    public static void viewMyApplications(Applicant applicant) {
        List<Project> projects = ProjectService.getVisibleProjects();
        ApplicantApplicationView.displayApplicationStatus(applicant);
        ApplicantApplicationView.showApplicationMenu(applicant, projects);
    }
}