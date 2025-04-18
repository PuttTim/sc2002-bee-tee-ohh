package views;

import java.util.List;

public class AuthView {
    public static void displayLoginHeader() {
        CommonView.displayHeader("Login");
    }

    public static String getNRIC() {
        return CommonView.prompt("Enter NRIC (or 'exit' to quit): ");
    }

    public static String getPassword() {
        return CommonView.prompt("Enter password: ");
    }

    public static void displayLoginSuccess(String name) {
        CommonView.displaySuccess("Welcome, " + name + "!");
    }

    public static void displayLoginFailed(String message) {
        CommonView.displayError("Login failed: " + message);
    }

    public static boolean promptRetryLogin() {
        return CommonView.promptYesNo("Would you like to try again?");
    }

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

    public static int showOfficerMenu() {
        List<String> options = List.of(
            "Register to Handle Project",
            "Check Registration Status",
            "View Handled Project Details",
            "Manage Project Enquiries",
            "Process Applications",
            "Generate Receipt"
            
        );
        return CommonView.displayMenuWithBacking("Officer Menu", options);
    }

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

    public static int showApplicantMainMenu() {
        List<String> options = List.of("Proceed to Applicant Menu", "Change Password", "Logout");
        return CommonView.displayMenu("Applicant Main Menu", options);
    }

    public static int showOfficerMainMenu() {
        List<String> options = List.of(
            "Applicant Mode",
            "Officer Mode",
            "Change Password",
            "Logout"
        );
        return CommonView.displayMenu("Main Menu", options);
    }

    public static int showManagerMainMenu() {
        List<String> options = List.of(
            "Proceed to Manager Menu",
            "Change Password",
            "Logout"
        );
        return CommonView.displayMenu("Main Menu", options);
    }

    public static void showChangePasswordHeader() {
        CommonView.displayHeader("Change Password");
    }

    public static String getProjectName() {
        return CommonView.prompt("Enter project name: ");
    }

    public static String getNewLocation() {
        return CommonView.prompt("Enter new location: ");
    }

    public static void displayPasswordChangeSuccess() {
        CommonView.displaySuccess("Password changed successfully!");
    }

    public static void displayPasswordChangeError(String message) {
        CommonView.displayError(message);
    }

    public static void displayProjectNotFound() {
        CommonView.displayError("Project not found!");
    }

    public static int showOfficerRegistrationMenu() {
        List<String> options = List.of(
            "View Officer Registrations",
            "Approve Officer Registration",
            "Reject/Remove Officer Registration"
        );
        return CommonView.displayMenu("Officer Registration Management", options);
    }

    public static boolean showTestingMenu() {
        System.out.println("\n=== Testing Menu ===");
        return CommonView.promptYesNo12("Would you like to use a test account?");
    }

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