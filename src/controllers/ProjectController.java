package controllers;

import models.Project;
import repositories.ProjectRepository;
import views.ProjectView;
import views.EnquiryView;
import java.util.List;

public class ProjectController {
    private final ProjectView projectView;
    private final EnquiryController enquiryController;

    public ProjectController() {
        this.projectView = new ProjectView();
        this.enquiryController = new EnquiryController(new EnquiryView());
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
                        System.out.println("Application feature not implemented yet");
                        break;
                }
            }
        }
    }
}
