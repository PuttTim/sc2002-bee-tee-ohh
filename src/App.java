import controllers.*;
import repositories.*;

/**
 * The main entry point of the housing project management application.
 * Initializes the repositories, runs the authentication process, and handles exceptions.
 */
// This is the main entrypoint of our application.
public class App {
    private App() {}

    public static void main(String[] args) {
        App app = new App();
        try {
            app.initRepositories();
            AuthController.runAuthentication();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // app.saveRepositories();
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
}
