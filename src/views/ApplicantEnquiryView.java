package views;

import models.Enquiry;
import models.Project;
import models.enums.EnquiryStatus;
import repositories.ProjectRepository;
import services.ApplicantEnquiryService;
import utils.DateTimeUtils;
import java.util.List;

public class ApplicantEnquiryView {
    public static void showEnquiryMenu() {
        List<String> options = List.of(
            "View My Enquiries",
            "Create New Enquiry",
            "Edit Enquiry",
            "Delete Enquiry",
            "Back to Main Menu"
        );
        CommonView.displayMenu("Enquiry Management", options);
    }

    public static void displayEnquiries(List<Enquiry> enquiries) {
        if (enquiries.isEmpty()) {
            CommonView.displayMessage("No enquiries found.");
            return;
        }

        CommonView.displayHeader("Your Enquiries");
        for (int i = 0; i < enquiries.size(); i++) {
            Enquiry enquiry = enquiries.get(i);
            CommonView.displayMessage(String.format("%d. Project ID: %s", i + 1, enquiry.getProjectID()));
            CommonView.displayMessage("   Query: " + enquiry.getQuery());
            CommonView.displayMessage("   Status: " + (enquiry.isResponse() ? "Responded" : "Pending"));
            if (enquiry.isResponse()) {
                CommonView.displayMessage("   Response: " + enquiry.getResponse());
            }
            CommonView.displaySeparator();
        }
    }

    public static Enquiry getEnquiryInput(String applicantNric, List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available for enquiry.");
            return null;
        }

        CommonView.displayHeader("Available Projects");
        for (int i = 0; i < projects.size(); i++) {
            CommonView.displayMessage(String.format("%d. %s", i + 1, projects.get(i).getProjectName()));
        }

        int projectChoice = CommonView.promptInt("\nSelect a project number (or 0 to cancel): ", 0, projects.size());
        if (projectChoice == 0) return null;

        Project selectedProject = projects.get(projectChoice - 1);
        String query = CommonView.prompt("\nEnter your enquiry: ");

        if (query.isEmpty()) {
            CommonView.displayError("Enquiry cannot be empty.");
            return null;
        }

        return new Enquiry(applicantNric, selectedProject.getProjectID(), query);
    }

    public static int getEnquiryToEditOrDelete(String action, int size) {
        int choice = CommonView.promptInt(
            String.format("Enter the number of the enquiry to %s (1-%d) or 0 to cancel: ", action, size),
            0, size);
        return choice - 1;
    }

    public static String getUpdatedContents() {
        String content = CommonView.prompt("Enter your updated enquiry (or press Enter to cancel): ");
        return content.isEmpty() ? null : content;
    }

    public static void showSuccess(String msg) {
        CommonView.displaySuccess(msg);
    }

    public static void showError(String msg) {
        CommonView.displayError(msg);
    }
}
