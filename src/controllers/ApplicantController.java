package controllers;

import models.*;
import repositories.ProjectRepository;
import views.*;
import java.util.Scanner;

public class ApplicantController {

    public static void newApplication(Applicant applicant) {
        ApplicantApplicationView.promptApplication(applicant, ProjectRepository.getAll());
    }

    public static void viewMyApplications(Applicant applicant) {
        ApplicantApplicationView.displayApplicationStatus(applicant);


    }
}
