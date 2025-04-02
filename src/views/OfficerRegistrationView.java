package views;

public class OfficerRegistrationView {
    public void displayRegistrationSuccess() {
        System.out.println("Your registration is successful. Please await the outcome.");
    }

    public void displayRegistrationFailure(String reason) {
        System.out.println("Failed to register. " + reason);
    }


}
