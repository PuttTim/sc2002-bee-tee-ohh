package controllers;

import enums.RegistrationStatus;
import interfaces.*;
import models.Officer;
import models.OfficerRegistration;
import models.Project;
import utils.CliUtils;

import java.util.List;


public class OfficerController {
    private final ApplicantController applicantController;
    private final IOfficerService officerService;
    private final IProjectService projectService;
    private final IOfficerRegistrationService officerRegistrationService;
    private final IOfficerRegistrationView officerRegistrationView;
    private final IProjectView projectView;
    private final ICommonView commonView;

    public OfficerController(
            ApplicantController applicantController,
            IOfficerService officerService,
            IProjectService projectService,
            IOfficerRegistrationService officerRegistrationService,
            IOfficerRegistrationView officerRegistrationView,
            IProjectView projectView,
            ICommonView commonView
    ) {
        this.applicantController = applicantController;
        this.officerService = officerService;
        this.projectService = projectService;
        this.officerRegistrationService = officerRegistrationService;
        this.officerRegistrationView = officerRegistrationView;
        this.projectView = projectView;
        this.commonView = commonView;
    }

    //register to join project as officer
    public void registerToHandleProject(Officer officer, Project project) {
        if (officerService.hasExistingProject(officer)) {
            officerRegistrationView.displayRegistrationFailure("You are already handling another project.");
            return;
        }

        if (officerService.hasExistingRegistration(officer)) {
            officerRegistrationView.displayRegistrationFailure("You already have an existing registration for another project.");
            return;
        }

        if (!projectService.hasOfficerSlots(project)) {
            officerRegistrationView.displayRegistrationFailure("Project has no more officer slots.");
            return;
        }
        OfficerRegistration officerRegistration = officerRegistrationService.createRegistration(officer, project);
        officerRegistrationView.displayRegistrationSuccess();
    }
    //check status of registration as officer
    public void checkHandlerRegistration(Officer officer) {
        RegistrationStatus registrationStatus = officerRegistrationService.checkRegistrationStatus(officer);
        officerRegistrationView.displayRegistrationStatus(registrationStatus);
    }
    //view details of project
    public void viewHandledProjectDetails(Officer officer) {
        List<Project> projects = projectService.findHandledProjects(officer);
        if (projects.isEmpty()) {
            projectView.displayEmptyMessage();
            return;
        }
        projectView.showProjectList(projects);
        int choice = CliUtils.promptInt("Enter the number of the project to view details (or 0 to cancel):");

        if (choice == 0) {
            commonView.displayMessage("Cacncelled");
            return;
        }

        if (choice < 1 || choice > projects.size()) {
            commonView.displayMessage("Invalid project number selected.");
            return;
        }

        Project selectedProject = projects.get(choice-1);
        projectView.displayProjectDetails(selectedProject);
    }
    //view and reply to enquiries
    public void viewProjectEnquiries() {
        //TODO

    }
    //help select flat
    public void processApplication() {
        //TODO
    }
    //generate receipt
    public void generateReceipt() {
        //TODO
    }
}
