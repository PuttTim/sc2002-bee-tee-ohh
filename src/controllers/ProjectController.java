package controllers;

import models.Project;
import repositories.ProjectRepository;
import views.ProjectView;
import views.EnquiryView;
import java.util.List;

public class ProjectController {
    private final ProjectView projectView;
    private final EnquiryController enquiryController;
    private final EnquiryView enquiryView;

    public ProjectController() {
        this.projectView = new ProjectView();
        this.enquiryView = new EnquiryView();
        this.enquiryController = new EnquiryController();


    }

    public void showMainMenu() {
        List<Project> projects = ProjectRepository.getAll();
        
        while (true) {
            projectView.showProjectList(projects);
            
            if (projects.isEmpty()) {
                System.out.println("No projects available.");
                return;
            }

            int projectChoice = projectView.getProjectChoice(projects.size());
            
            if (projectChoice == 0) {
                return;
            }

            Project selectedProject = projects.get(projectChoice - 1);
            
            boolean continueProjectMenu = true;
            while (continueProjectMenu) {
                int menuChoice = projectView.getProjectMenuChoice();
                
                switch (menuChoice) {
                    case 0:
                        continueProjectMenu = false;
                        break;
                    case 1:
                        enquiryController.listEnquiries(selectedProject);
                        break;
                    case 2:
                        String query = this.enquiryView.getEnquiryDetails();
                        // TODO: Get actual logged in user's NRIC
                        String tempNric = "S1234567A";
                        enquiryController.createEnquiry(selectedProject, tempNric, query);
                        break;
                    default:
                        System.out.println("Application feature not implemented yet");
                        break;
                }
            }
        }
    }
}
