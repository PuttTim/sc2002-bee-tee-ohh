package controllers;

import models.Officer;
import models.OfficerApplication;
import models.Project;
import services.OfficerService;
import services.ProjectService;
import services.OfficerApplicationService;

public class OfficerController {
    private final OfficerService officerService;
    private final ProjectService projectService;
    private final OfficerApplicationService  officerApplicationService;

    public OfficerController(OfficerService officerService, ProjectService projectService, OfficerApplicationService officerApplicationService) {
        this.officerService = officerService;
        this.projectService = projectService;
        this.officerApplicationService = officerApplicationService;
    }
    //all applicants capabilities
    //can only apply to projects not officer in - handle in applyproject

    //register to join project as officer
    public void registerToHandleProject(Officer officer, Project project) {
        if (officerService.officerHasExistingProject(officer)) {
            System.out.println("Failed to register. You are already handling another project.");
        } else {
            boolean success = projectService.registerForOfficer(officer, project);
            if (success) {
                OfficerApplication officerApplication = officerApplicationService.createApplication(officer, project);
                officer.setOfficerApplication(officerApplication);
                System.out.println("Your registration is successful. Please await the outcome.");
            }
        }

    }
    //check status of registration as officer
    public void checkHandlerApplication(Officer officer) {
        //TODO
        OfficerApplication officerApplication = officer.getOfficerApplication();
        System.out.println(officerApplication.getApplicationOutcome());
    }
    //view details of project
    public void viewHandledProjectDetails() {
        //TODO
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
