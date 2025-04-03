import controllers.ProjectController;
import models.Project;
import repositories.ProjectRepository;
import services.ProjectService;
import views.ProjectView;

import controllers.EnquiryController;
import repositories.EnquiryRepository;
import services.EnquiryService;
import views.EnquiryView;

// This is the main entrypoint of our application.
public class App {
    private App() {
    };

    public static void main(String[] args) {
        try {
            App app = new App();
            app.initRepositories();

            ProjectController projectController = new ProjectController();
            projectController.showMainMenu();

            app.saveRepositories();

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private void initRepositories() {
        // Initialize repositories
        ProjectRepository.load();
        EnquiryRepository.load();
    }

    private void saveRepositories() {
        // Save repositories

        ProjectRepository.saveAll();  
    }
}
