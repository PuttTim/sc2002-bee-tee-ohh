package services;

import models.*;
import models.enums.ApplicationStatus;
import models.enums.FlatType;
import models.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicantApplicationService {
    private static int applicationIdCount = 1; //for generating application id???
    private List<Application> applications = new ArrayList<>(); //to store all applications

    private String generateApplicationId() {
        return "A" + applicationIdCount++; //A1, A2, A3, etc
    }

    public List<Project> viewEligibleProjects(Applicant applicant, List<Project> allProjects) {
        List<Project> eligibleProjects = new ArrayList<>();

        for (Project project : allProjects) {
            if (applicant.getMaritalStatus() == MaritalStatus.SINGLE && applicant.getAge() >= 35 && project.isVisible()) {
                //single and over 35, can only apply 2-room
                if (project.getProjectName().equals("2-room")) {
                    eligibleProjects.add(project);
                }
            } else if (applicant.getMaritalStatus() == MaritalStatus.MARRIED && applicant.getAge() >= 21 && project.isVisible()) {
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
        FlatType chosenFlatType;
        try {
            chosenFlatType = FlatType.valueOf(selectFlatType.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid flat type string provided.");
        }

        // TODO: Check project flat types list contains the chosenFlatType
        // TODO: Check project flat type units > 0 for the chosenFlatType

        if ((applicant.getMaritalStatus() == MaritalStatus.SINGLE && chosenFlatType == FlatType.TWO_ROOM) ||
                (applicant.getMaritalStatus() == MaritalStatus.MARRIED && (chosenFlatType == FlatType.TWO_ROOM || chosenFlatType == FlatType.THREE_ROOM))) {
            
            // TODO: Check if applicant already has an existing application

            Application application = new Application(applicant.getUserNRIC(), project.getProjectID(), chosenFlatType);
            // TODO: Add application to repository
            // TODO: Add applicant NRIC to project's applicant list in repository
            // TODO: Decrement flat count in project repository
            
            // Example: applications.add(application); ProjectRepository.getById(project.getProjectID()).addApplicant(applicant.getNric()); etc.
            
            return application;
        } else {
            // More specific error messages could be useful here
            throw new IllegalArgumentException("Applicant eligibility criteria not met for the selected flat type.");
        }
    }

    //get applications by applicant
    public List<Application> getApplicationsByApplicant(Applicant applicant) {
        // TODO: Implement logic to retrieve applications from ApplicationRepository based on applicant NRIC
        // Example: return ApplicationRepository.getByApplicantNric(applicant.getNric());
        return applications.stream()
               .filter(app -> app.getApplicantNRIC().equals(applicant.getUserNRIC()))
               .collect(Collectors.toList()); // Placeholder implementation
    }

    //view status
    public ApplicationStatus viewApplicationStatus(Application application) {
        return application.getApplicationStatus();
    }

    //withdraw application
    public void withdrawApplication(Application application) {
        //can application be withdrawn during pending???
        // application.withdrawApplication();
    }
}
