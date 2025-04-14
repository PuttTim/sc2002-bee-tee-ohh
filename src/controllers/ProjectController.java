package controllers;

import models.Project;
import repositories.ProjectRepository;
import views.ProjectView;
import views.EnquiryView;
import java.util.List;

public class ProjectController {
    public static void showMainMenu() {
        List<Project> projects = ProjectRepository.getAll();
        
        while (true) {
            ProjectView.showProjectList(projects);
            
            if (projects.isEmpty()) {
                System.out.println("No projects available.");
                return;
            }

            int projectChoice = ProjectView.getProjectChoice(projects.size());
            
            if (projectChoice == 0) {
                return;
            }

            Project selectedProject = projects.get(projectChoice - 1);
            
            boolean continueProjectMenu = true;
            while (continueProjectMenu) {
                int menuChoice = ProjectView.getProjectMenuChoice();
                
                switch (menuChoice) {
                    case 0:
                        continueProjectMenu = false;
                        break;
                    case 1:
                        EnquiryController.listEnquiries(selectedProject);
                        break;
                    case 2:
                        String query = EnquiryView.getEnquiryDetails();
                        // TODO: Get actual logged in user's NRIC
                        String tempNric = "S1234567A";
                        EnquiryController.createEnquiry(selectedProject, tempNric, query);
                        break;
                    default:
                        System.out.println("Application feature not implemented yet");
                        break;
                }
            }
        }
    }
}
