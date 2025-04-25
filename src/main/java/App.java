import controllers.*;
import repositories.*;
import services.*;
import exceptions.AuthenticationException;

/**
 * The main entry point of the housing project management application.
 * Initializes the repositories, services, controllers and runs the authentication process.
 */
public class App {
    private static AuthController authController;

    private App() {
        initializeApplication();
    }

    private void initializeApplication() {
        try {
            initRepositories();
            initServices();
            initControllers();
        } catch (Exception e) {
            System.err.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initRepositories() {
        UserRepository.load();
        ProjectRepository.load();
        EnquiryRepository.load();
        RegistrationRepository.load();
        ApplicantRepository.load();
        ApplicationRepository.load();
        ReceiptRepository.load();
        OfficerRepository.load();
        ManagerRepository.load();
    }

    private void initServices() {
        // Initialize services
        ApplicationService.getInstance();
        ProjectService.getInstance();
        EnquiryService.getInstance();
        RegistrationService.getInstance();
        ApplicantApplicationService.getInstance();
        AuthService.getInstance();
    }

    private void initControllers() {
        authController = new AuthController();
    }

    private void saveRepositories() {
        UserRepository.saveAll();
        ProjectRepository.saveAll();
        EnquiryRepository.saveAll();
        RegistrationRepository.saveAll();
        ApplicantRepository.saveAll();
        ApplicationRepository.saveAll();
        OfficerRepository.saveAll();
        ManagerRepository.saveAll();
        ReceiptRepository.saveAll();
    }

    /**
     * The main method that starts the application.
     * Initializes the application components and triggers the authentication process.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        App app = new App();
        try {
            authController.runAuthentication();
        } catch (AuthenticationException e) {
            System.err.println("Authentication error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            app.saveRepositories();
        }
    }
}
