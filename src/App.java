import controllers.*;
import repositories.*;

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
        ProjectRepository.load();
        EnquiryRepository.load();
        UserRepository.load();
        RegistrationRepository.load();
        ApplicantRepository.load();
        ApplicationRepository.load();
        OfficerRepository.load();
        ManagerRepository.load();
    }

    private void saveRepositories() {
        ProjectRepository.saveAll();
        EnquiryRepository.saveAll();
        UserRepository.saveAll();
        RegistrationRepository.saveAll();
        ApplicantRepository.saveAll();
        ApplicationRepository.saveAll();
        OfficerRepository.saveAll();
        ManagerRepository.saveAll();
    }
}
