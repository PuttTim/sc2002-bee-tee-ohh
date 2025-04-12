package controllers;

import interfaces.*;
import models.Enquiry;
import models.Officer;
import models.OfficerRegistration;
import models.Project;
import models.enums.RegistrationStatus;
import utils.CliUtils;

import java.util.List;
import java.util.Optional;


public class OfficerController {
    private final ApplicantController applicantController;
    private final IOfficerService officerService;
    private final IProjectService projectService;
    private final IOfficerRegistrationService officerRegistrationService;
    private final IOfficerRegistrationView officerRegistrationView;
    private final IProjectView projectView;
    private final ICommonView commonView;
    private final IEnquiryView enquiryView;
    private final IEnquiryService enquiryService;

    public OfficerController(
            ApplicantController applicantController,
            IOfficerService officerService,
            IProjectService projectService,
            IOfficerRegistrationService officerRegistrationService,
            IOfficerRegistrationView officerRegistrationView,
            IProjectView projectView,
            ICommonView commonView,
            IEnquiryView enquiryView,
            IEnquiryService enquiryService
    ) {
        this.applicantController = applicantController;
        this.officerService = officerService;
        this.projectService = projectService;
        this.officerRegistrationService = officerRegistrationService;
        this.officerRegistrationView = officerRegistrationView;
        this.projectView = projectView;
        this.commonView = commonView;
        this.enquiryView = enquiryView;
        this.enquiryService = enquiryService;
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
        RegistrationStatus registrationStatus = officerRegistrationService.getRegistrationStatus(officer);
        officerRegistrationView.displayRegistrationStatus(registrationStatus);
    }
    private Optional<Project> getProjectSelection(Officer officer) {
        List<Project> projects = projectService.getHandledProjects(officer);
        if (projects.isEmpty()) {
            projectView.displayEmptyMessage();
            return Optional.empty();
        }
        projectView.showProjectList(projects);
        int choice = CliUtils.promptInt("Enter the number of the project to view details (or 0 to cancel):");

        if (choice == 0) {
            commonView.displayMessage("Cancelled.");
            return Optional.empty();
        }

        if (choice < 1 || choice > projects.size()) {
            commonView.displayMessage("Invalid project number selected.");
            return Optional.empty();
        }
        Project selectedProject = projects.get(choice-1);
        return Optional.of(selectedProject);
    }
    //view details of project
    public void viewHandledProjectDetails(Officer officer) {
        Optional<Project> selectedProjectOptional = getProjectSelection(officer);
        if (selectedProjectOptional.isPresent()) {
            Project selectedProject = selectedProjectOptional.get();
            projectView.displayProjectDetails(selectedProject);
        }
    }
    //view and reply to enquiries
    public void manageProjectEnquiries(Officer officer) {
        //TODO
        Optional<Project> selectedProjectOptional = getProjectSelection(officer);
        if (selectedProjectOptional.isEmpty()) {
            return;
        }
        Project selectedProject = selectedProjectOptional.get();
        List<Enquiry> enquiries = enquiryService.getProjectEnquiries(selectedProject);
        if (enquiries.isEmpty()) {
            enquiryView.displayEmptyMessage();
            return;
        }
        enquiryView.showEnquiryList(enquiries);
        int choice = CliUtils.promptInt("Enter the number of the enquiry to view details/reply (or 0 to cancel):");

        if (choice == 0) {
            commonView.displayMessage("Cancelled.");
            return;
        }

        if (choice < 1 || choice > enquiries.size()) {
            commonView.displayMessage("Invalid project number selected.");
            return;
        }
        Enquiry selectedEnquiry = enquiries.get(choice-1);

        enquiryView.displayEnquiry(selectedEnquiry);

        if (CliUtils.promptYesNo("Do you want to reply to this enquiry?")) {
            enquiryService.replyToEnquiry(selectedEnquiry);
        } else {
            commonView.displayMessage("Exiting...");
        }

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
