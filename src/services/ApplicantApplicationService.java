package services;

import models.*;
import enums.ApplicationStatus;

import java.util.ArrayList;
import java.util.List;

public class ApplicantApplicationService {
    private static int applicationIdCount = 1; //for generating application id???

    private String generateApplicationId() {
        return "A" + applicationIdCount++; //A1, A2, A3, etc
    }

    public List<Project> viewEligibleProjects(Applicant applicant, List<Project> allProjects) {
        List<Project> eligibleProjects = new ArrayList<>();

        for (Project project : allProjects) {
            if (applicant.getMaritalStatus().equals("Single") && applicant.getAge() >= 35 && project.isVisible()) {
                //single and over 35, can only apply 2-room
                if (project.getProjectName().equals("2-room")) {
                    eligibleProjects.add(project);
                }
            } else if (applicant.getMaritalStatus().equals("Married") && applicant.getAge() >= 21 && project.isVisible()) {
                //married and over 21, can apply to anything
                eligibleProjects.add(project);
            }
        }
        return eligibleProjects;
    }

    //application section
    //apply for project
    public Application applyForProject(Applicant applicant, Project project, String selectFlatType) {
        //check if selected flat type is valid
        //valid if applicant chose 2-room
        //valid if applicant is married and chose 2/3-room
        if ((applicant.getMaritalStatus().equals("Single") && selectFlatType.equals("2-Room")) ||
                (applicant.getMaritalStatus().equals("Married") && (selectFlatType.equals("2-Room") || selectFlatType.equals("3-Room")))) {
            String applicationId = generateApplicationId(); //generate new application id
            Application application = new Application(applicationId, applicant, project, selectFlatType, false, null);
            //not sure about false + null
            return application;
        } else {
            throw new IllegalArgumentException("Invalid flat type chosen.");
        }
    }

    //applicant views their application status
    public ApplicationStatus viewApplicationStatus(Application application) {
        return application.getApplicationStatus();
    }

    //withdraw application
    public void withdrawApplication(Application application) {
        //can application be withdrawn during pending???
        application.withdrawApplication();
    }
}
