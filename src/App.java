import controllers.ProjectController;
import repositories.ProjectRepository;
import repositories.EnquiryRepository;

// This is the main entrypoint of our application.
public class App {
    private App() {
    };

    public static void main(String[] args) {
        App app = new App();
        try {
            app.initRepositories();

            ProjectController projectController = new ProjectController();
            projectController.showMainMenu();

            app.saveRepositories();

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());

            app.saveRepositories();
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
        EnquiryRepository.saveAll();
    }
}
