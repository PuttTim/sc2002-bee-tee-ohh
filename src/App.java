import controllers.ProjectController;
import repositories.ProjectRepository;
import repositories.RegistrationRepository;
import repositories.UserRepository;
import repositories.EnquiryRepository;

// This is the main entrypoint of our application.
public class App {
    private App() {
    };

    public static void main(String[] args) {
        App app = new App();
        try {
            app.initRepositories();

            // ProjectController projectController = new ProjectController();
            // projectController.showMainMenu();

            System.out.println(RegistrationRepository.getAll().get(1).getProjectID());

            // app.saveRepositories();

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            System.out.println("Error: " + e.getLocalizedMessage() + e.getStackTrace().toString());

            app.saveRepositories();
        }
    }

    private void initRepositories() {
        // Initialize repositories
        ProjectRepository.load();
        EnquiryRepository.load();
        UserRepository.load();
        RegistrationRepository.load();
    }

    private void saveRepositories() {
        // Save repositories

        ProjectRepository.saveAll();  
        EnquiryRepository.saveAll();
        UserRepository.saveAll();
        RegistrationRepository.saveAll();
    }
}
