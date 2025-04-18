package controllers;

import models.Officer;
import models.Project;
import services.*;
import views.*;

public class OfficerController {
    //register to join project as officer
    public static void registerToHandleProject(Officer officer, Project project) {
        if (OfficerService.hasExistingProject(officer)) {
            CommonView.displayError("You are already handling another project.");
            return;
        }

        if (OfficerService.hasExistingRegistration(officer)) {
            CommonView.displayError("You already have an existing registration for another project.");
            return;
        }

        ProjectController.handleOfficerRegistration(officer, project.getProjectName());
    }

    public static void checkHandlerRegistration(Officer officer) {
        if (OfficerService.hasExistingProject(officer)) {
            Project project = ProjectService.getProjectByOfficer(officer);
            if (project != null) {
                CommonView.displayMessage("You are currently handling project: " + project.getProjectName());
            }
        } else if (OfficerService.hasExistingRegistration(officer)) {
            CommonView.displayMessage("You have a pending registration request.");
        } else {
            CommonView.displayMessage("You have no active registrations.");
        }
    }

    //view details of project
    public static void viewHandledProjectDetails(Officer officer) {
        Project project = ProjectService.getProjectByOfficer(officer);
        if (project != null) {
            ProjectController.viewProjectEnquiries(project.getProjectName());
        } else {
            CommonView.displayError("You do not have any projects assigned to you.");
        }
    }

    //view and reply to enquiries
    public static void manageProjectEnquiries(Officer officer) {
        Project project = ProjectService.getProjectByOfficer(officer);
        if (project != null) {
            ProjectController.viewProjectEnquiries(project.getProjectName());
        } else {
            CommonView.displayError("You are not handling any project.");
        }
    }

    //help select flat
    public static void processApplication() {
        //TODO
    }

    //generate receipt
    public static void generateReceipt() {
        //TODO
    }
}
