package controllers;

import models.Enquiry;
import models.Officer;
import models.Project;
import repositories.ProjectRepository;
import services.*;
import views.*;

import java.util.List;
import java.util.Optional;

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

        if (!ProjectService.hasOfficerSlots(project)) {
            CommonView.displayError("Project has no more officer slots.");
            return;
        }
        CommonView.displaySuccess("Officer registration submitted successfully.");
    }

    public static void checkHandlerRegistration(Officer officer) {
        if (OfficerService.hasExistingProject(officer)) {
            Project project = ProjectService.getProjectByOfficer(officer);
            if (project != null) {
                CommonView.displaySuccess("You are currently handling project: " + project.getProjectName());
            }
        } else if (OfficerService.hasExistingRegistration(officer)) {
            CommonView.displayMessage("You have a pending registration request.");
        } else {
            CommonView.displayMessage("You have no active registrations.");
        }
    }

    private static Optional<Project> getProjectSelection(Officer officer) {
        List<Project> availableProjects = ProjectRepository.getAll().stream()
            .filter(Project::isVisible)
            .toList();

        if (availableProjects.isEmpty()) {
            CommonView.displayMessage("No projects available for registration.");
            return Optional.empty();
        }

        ProjectView.showProjectList(availableProjects);
        int choice = CommonView.promptInt("Select a project number (or 0 to cancel): ");

        if (choice == 0) {
            return Optional.empty();
        }

        if (choice < 1 || choice > availableProjects.size()) {
            CommonView.displayError("Invalid project selection.");
            return Optional.empty();
        }

        return Optional.of(availableProjects.get(choice - 1));
    }

    //view details of project
    public static void viewHandledProjectDetails(Officer officer) {
        Optional<Project> selectedProjectOptional = getProjectSelection(officer);
        if (selectedProjectOptional.isPresent()) {
            Project selectedProject = selectedProjectOptional.get();
            ProjectView.displayProjectDetails(selectedProject);
        }
    }

    //view and reply to enquiries
    public static void manageProjectEnquiries(Officer officer) {
        Optional<Project> selectedProjectOptional = getProjectSelection(officer);
        if (selectedProjectOptional.isEmpty()) {
            return;
        }
        Project selectedProject = selectedProjectOptional.get();
        EnquiryController.manageProjectEnquiries(Optional.of(officer), Optional.empty(), selectedProject);
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
