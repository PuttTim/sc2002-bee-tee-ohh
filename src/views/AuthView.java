package views;

import java.util.List;

/**
 * View class for authentication-related interactions and role-based menus.
 */
public class AuthView {

    /** Displays the login header. */
    public static void displayLoginHeader() {
        CommonView.displayHeader("Login");
    }

    /**
     * Prompts the user to enter their NRIC.
     *
     * @return the input NRIC or "exit" to quit
     */
    public static String getNRIC() {
        return CommonView.prompt("Enter NRIC (or 'exit' to quit): ");
    }

    /**
     * Prompts the user to enter their password.
     *
     * @return the input password
     */
    public static String getPassword() {
        return CommonView.prompt("Enter password: ");
    }

    /**
     * Displays a login success message.
     *
     * @param name the name of the logged-in user
     */
    public static void displayLoginSuccess(String name) {
        CommonView.displaySuccess("Welcome, " + name + "!");
    }

    /**
     * Displays a login failure message.
     *
     * @param message the reason for login failure
     */
    public static void displayLoginFailed(String message) {
        CommonView.displayError("Login failed: " + message);
    }

    /**
     * Prompts the user whether they want to retry login.
     *
     * @return true if retrying, false otherwise
     */
    public static boolean promptRetryLogin() {
        return CommonView.promptYesNo("Would you like to try again?");
    }

    /**
     * Displays the menu for an applicant user.
     *
     * @return the selected menu option
     */
    public static int showApplicantMenu() {
        List<String> options = List.of(
            "View Available Projects",
            "Manage My Applications",
            "View My Enquiries",
            "Create New Enquiry"
        );
        return CommonView.displayMenuWithBacking("Applicant Menu", options);
    }

    /**
     * Displays the menu for an officer user.
     *
     * @return the selected menu option
     */
    public static int showOfficerMenu() {
        List<String> options = List.of(
            "Register to Handle Project",
            "Check Registration Status",
            "View All Handled Projects" 
        );
        return CommonView.displayMenuWithBacking("Officer Menu", options);
    }

    /**
     * Displays the menu for a manager user.
     *
     * @return the selected menu option
     */
    public static int showManagerMenu() {
        List<String> options = List.of(
            "View All Projects",
            "View All Managed Projects",
            "View All Enquiries",
            "Create New Project"
        );
        return CommonView.displayMenuWithBacking("Manager Menu", options);
    }

    /**
     * Displays the main menu for an applicant.
     *
     * @return the selected menu option
     */
    public static int showApplicantMainMenu() {
        List<String> options = List.of("Proceed to Applicant Menu", "Change Password", "Logout");
        return CommonView.displayMenu("Applicant Main Menu", options);
    }

    /**
     * Displays the main menu for an officer.
     *
     * @return the selected menu option
     */
    public static int showOfficerMainMenu() {
        List<String> options = List.of(
                "Applicant Mode",
                "Officer Mode",
                "Change Password",
                "Logout"
        );
        return CommonView.displayMenu("Main Menu", options);
    }

    /**
     * Displays the main menu for a manager.
     *
     * @return the selected menu option
     */
    public static int showManagerMainMenu() {
        List<String> options = List.of(
                "Proceed to Manager Menu",
                "Change Password",
                "Logout"
        );
        return CommonView.displayMenu("Main Menu", options);
    }

    /** Displays the header for the change password screen. */
    public static void showChangePasswordHeader() {
        CommonView.displayHeader("Change Password (0 to cancel)");
    }

    /**
     * Prompts the user to enter a project name.
     *
     * @return the input project name
     */
    public static String getProjectName() {
        return CommonView.prompt("Enter project name: ");
    }

    /**
     * Prompts the user to enter a new project location.
     *
     * @return the input location
     */
    public static String getNewLocation() {
        return CommonView.prompt("Enter new location: ");
    }

    /** Displays a success message after a password change. */
    public static void displayPasswordChangeSuccess() {
        CommonView.displaySuccess("Password changed successfully!");
    }

    /**
     * Displays an error message for password change failure.
     *
     * @param message the error message
     */
    public static void displayPasswordChangeError(String message) {
        CommonView.displayError(message);
    }

    /** Displays an error message when a project is not found. */
    public static void displayProjectNotFound() {
        CommonView.displayError("Project not found!");
    }

    /**
     * Displays the officer registration management menu.
     *
     * @return the selected menu option
     */
    public static int showOfficerRegistrationMenu() {
        List<String> options = List.of(
                "View Officer Registrations",
                "Approve Officer Registration",
                "Reject/Remove Officer Registration"
        );
        return CommonView.displayMenu("Officer Registration Management", options);
    }

    /**
     * Displays a testing menu for optional test account login.
     *
     * @return true if user opts to use a test account
     */
    public static boolean showTestingMenu() {
        System.out.println("\n=== Testing Menu ===");
        return CommonView.promptYesNo12("Would you like to use a test account?");
    }

    /**
     * Displays options for selecting a test user.
     *
     * @return the chosen test user type (1-3)
     */
    public static int showTestUserOptions() {
        System.out.println("\n=== Test Users ===");
        System.out.println("1. Test Applicant");
        System.out.println("2. Test Officer");
        System.out.println("3. Test Manager");
        while (true) {
            int choice = CommonView.promptInt("Enter your choice (1-3): ", 1, 3);
            if (choice >= 1 && choice <= 3) {
                return choice;
            }
            CommonView.displayError("Invalid choice. Please enter a number between 1 and 3.");
        }
    }
}
