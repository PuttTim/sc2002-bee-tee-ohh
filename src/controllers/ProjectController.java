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

public class ProjectController {

    public static void viewAvailableProjects(Applicant applicant) {
        List<Project> projects = ProjectService.getVisibleProjects();
        CommonView.displayHeader("Available Projects");
        ProjectView.displayAvailableProjects(projects);
        ProjectView.showProjectMenu(applicant, projects);
    }

    public static void deleteProject() {
        String projectName = ProjectView.getProjectName();
        ProjectService.deleteProject(projectName);
        ProjectView.displayProjectDeleteSuccess();
    }

    public static void toggleProjectVisibility() {
        String projectName = ProjectView.getProjectName();
        boolean visibility = ProjectView.getProjectVisibility();
        ProjectService.toggleProjectVisibility(projectName, visibility);
        ProjectView.displayVisibilityUpdateSuccess();
    }

    public static void viewAllProjects() {
        List<Project> projects = ProjectService.getAllProjects();
        CommonView.displayHeader("Available Projects");
        ProjectView.displayAvailableProjects(projects);
    }

    public static void viewProjectEnquiries(Project project) {
        List<Enquiry> enquiries = EnquiryService.getProjectEnquiries(project);
        if (enquiries.isEmpty()) {
            CommonView.displayMessage("No enquiries for this project.");
        } else {
            EnquiryView.displayEnquiryList(enquiries);
        }
    }

    public static void viewOfficerRegistrations() {
        List<Project> projects = ProjectService.getAllProjects();
        // ProjectView.displayOfficerRegistrations(projects);
    }

    public static void handleOfficerRegistration(Officer officer, String projectName) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project == null) {
            ProjectView.displayProjectNotFound();
            return;
        }
        
        if (!ProjectService.hasOfficerSlots(project)) {
            CommonView.displayError("Project has no more officer slots.");
            return;
        }

        ProjectService.addOfficerToProject(project, officer.getUserNRIC());
        CommonView.displaySuccess("Officer registration submitted successfully.");
    }

    public static void removeOfficerFromProject(String projectName, String officerNRIC) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project != null) {
            ProjectService.removeOfficerFromProject(project, officerNRIC);
        } else {
            ProjectView.displayProjectNotFound();
        }
    }
}
