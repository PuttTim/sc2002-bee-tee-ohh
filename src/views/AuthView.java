package views;

import java.util.List;

/**
 * A view class that handles the display and user interaction for authentication-related actions
 * such as login and menu navigation.
 */
public class AuthView {

    /**
     * Displays the login header.
     */
    public static void displayLoginHeader() {
        CommonView.displayHeader("Login");
    }

    /**
     * Prompts the user to enter their NRIC.
     *
     * @return the entered NRIC or exit if the user wants to quit
     */
    public static String getNRIC() {
        return CommonView.prompt("Enter NRIC (or 'exit' to quit): ");
    }

    /**
     * Prompts the user to enter their password.
     *
     * @return the entered password
     */
    public static String getPassword() {
        return CommonView.prompt("Enter password: ");
    }

    /**
     * Displays a success message when login is successful.
     *
     * @param name the name of the logged-in user
     */
    public static void displayLoginSuccess(String name) {
        CommonView.displaySuccess("Welcome, " + name + "!");
    }

    /**
     * Displays a failure message when login fails.
     *
     * @param message the error message explaining why the login failed
     */
    public static void displayLoginFailed(String message) {
        CommonView.displayError("Login failed: " + message);
    }

    /**
     * Prompts the user to retry login.
     *
     * @return <code>true</code> if the user wants to try again, <code>false</code> if not
     */
    public static boolean promptRetryLogin() {
        return CommonView.promptYesNo("Would you like to try again?");
    }

    /**
     * Displays the applicant menu.
     *
     * @return the selected menu option
     */
    public static int showApplicantMenu() {
        List<String> options = List.of(
                "View Available Projects",
                "Submit Project Application",
                "View My Applications",
                "Create New Enquiry",
                "View My Enquiries"
        );
        return CommonView.displayMenuWithBacking("Applicant Menu", options);
    }

    /**
     * Displays the officer menu.
     *
     * @return the selected menu option
     */
    public static int showOfficerMenu() {
        List<String> options = List.of(
                "Register to Handle Project",
                "Check Registration Status",
                "View Handled Projects",
                "Manage Project Enquiries",
                "Process Applications",
                "Generate Receipt"
        );
        return CommonView.displayMenuWithBacking("Officer Menu", options);
    }

    /**
     * Displays the manager menu.
     *
     * @return the selected menu option
     */
    public static int showManagerMenu() {
        List<String> options = List.of(
                "Create New Project",
                "Edit Project",
                "Delete Project",
                "Toggle Project Visibility",
                "View All Projects",
                "View Project Enquiries",
                "Manage Officer Registrations"
        );
        return CommonView.displayMenuWithBacking("Manager Menu", options);
    }

    /**
     * Displays the applicant main menu.
     *
     * @return the selected menu option
     */
    public static int showApplicantMainMenu() {
        List<String> options = List.of("Proceed to Applicant Menu", "Change Password", "Logout");
        return CommonView.displayMenu("Applicant Main Menu", options);
    }

    /**
     * Displays the officer main menu.
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
     * Displays the manager main menu.
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

    /**
     * Displays the header for the change password section.
     */
    public static void showChangePasswordHeader() {
        CommonView.displayHeader("Change Password (0 to cancel)");
    }

    /**
     * Prompts the user to enter the project name.
     *
     * @return the entered project name
     */
    public static String getProjectName() {
        return CommonView.prompt("Enter project name: ");
    }

    /**
     * Prompts the user to enter a new location.
     *
     * @return the entered new location
     */
    public static String getNewLocation() {
        return CommonView.prompt("Enter new location: ");
    }

    /**
     * Displays a success message when the password change is successful.
     */
    public static void displayPasswordChangeSuccess() {
        CommonView.displaySuccess("Password changed successfully!");
    }

    /**
     * Displays an error message when there is an issue with the password change.
     *
     * @param message the error message
     */
    public static void displayPasswordChangeError(String message) {
        CommonView.displayError(message);
    }

    /**
     * Displays an error message when a project is not found.
     */
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
     * Displays the testing menu and asks the user if they want to use a test account.
     *
     * @return <code>true</code> if the user wants to use a test account,
     * <code>false</code> if not
     */
    public static boolean showTestingMenu() {
        System.out.println("\n=== Testing Menu ===");
        return CommonView.promptYesNo12("Would you like to use a test account?");
    }

    /**
     * Displays test user options and prompts the user to select one.
     *
     * @return the selected test user option
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
