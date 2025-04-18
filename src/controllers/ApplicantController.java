package controllers;

import models.*;
import repositories.ProjectRepository;
import views.*;
import java.util.List;

public class ApplicantController {

    public static void newApplication(Applicant applicant) {
        ApplicantApplicationView.promptApplication(applicant, ProjectRepository.getAll());
    }

    public static void viewMyApplications(Applicant applicant) {
        List<Project> projects = ProjectRepository.getAll();
        ApplicantApplicationView.displayApplicationStatus(applicant);
        ApplicantApplicationView.showApplicationMenu(applicant, projects);
    }
}