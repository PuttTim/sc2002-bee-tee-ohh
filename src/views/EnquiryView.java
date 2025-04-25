package views;

import controllers.EnquiryController;
import models.Applicant;
import models.Enquiry;
import models.Project;
import repositories.ProjectRepository;
import repositories.UserRepository;
import utils.DateTimeUtils;

import java.util.List;

/**
 * View class for displaying and managing enquiry-related views.
 */
public class EnquiryView {
    private static final EnquiryController enquiryController = new EnquiryController();

    /**
     * Displays the enquiry menu for an applicant.
     *
     * @param applicant the currently logged-in applicant
     */
    public static void showEnquiryMenu(Applicant applicant) {
        List<String> options = List.of(
                "Create New Enquiry",
                "Edit Enquiry",
                "Delete Enquiry"
        );

        while (true) {
            int choice = CommonView.displayMenuWithBacking("Enquiry Menu", options);
            try {
                switch (choice) {
                    case 1 -> enquiryController.createNewEnquiry(applicant);
                    case 2 -> enquiryController.editEnquiry(applicant);
                    case 3 -> enquiryController.deleteEnquiry(applicant);
                    case 0 -> {
                        return;
                    }
                }
            } catch (Exception e) {
                EnquiryView.displayError(e.getMessage());
            }
        }
    }

    /**
     * Displays a list of enquiries.
     *
     * @param enquiries the list of enquiries to display
     */
    public static void displayEnquiryList(List<Enquiry> enquiries) {
        if (enquiries.isEmpty()) {
            displayEmptyMessage();
            return;
        }

        CommonView.displayHeader("Enquiries");
        for (int i = 0; i < enquiries.size(); i++) {
            Enquiry enquiry = enquiries.get(i);
            Project project = ProjectRepository.getById(enquiry.getProjectID());

            CommonView.displayMessage(i + 1 + ". " + "Enquiry ID: " + enquiry.getEnquiryID());
            CommonView.displayMessage(String.format("   From: %s", UserRepository.getByNRIC(enquiry.getApplicantNRIC()).getName()));
            CommonView.displayMessage("   Enquiry Date: " + DateTimeUtils.formatDateTime(enquiry.getEnquiryDate()));
            CommonView.displayMessage("   Query: " + enquiry.getQuery());
            CommonView.displayMessage("   Status: " + (enquiry.isResponse() ? "Responded" : "Pending"));
            if (enquiry.isResponse()) {
                CommonView.displayMessage("   Response: " + enquiry.getResponse());
                CommonView.displayMessage("   Responded by: " + UserRepository.getByNRIC(enquiry.getResponder()).getName());
            }
            CommonView.displayMessage("   Last Updated: " + DateTimeUtils.formatDateTime(enquiry.getLastUpdated()));
            CommonView.displayMessage("   Project: " + project.getProjectName());

            System.out.println("-----------------------------------");
            System.out.println();
        }
    }

    /**
     * Displays the details of a single enquiry.
     *
     * @param enquiry the enquiry to display
     */
    public static void displayEnquiry(Enquiry enquiry) {
        CommonView.displayHeader("Enquiry Details");
        CommonView.displayMessage("Enquiry ID: " + enquiry.getEnquiryID());
        CommonView.displayMessage("From: " + enquiry.getApplicantNRIC());
        CommonView.displayMessage("Project ID: " + enquiry.getProjectID());
        CommonView.displayMessage("Query: " + enquiry.getQuery());
        if (enquiry.isResponse()) {
            CommonView.displayMessage("Response: " + enquiry.getResponse());
            CommonView.displayMessage("Responded by: " + enquiry.getResponder());
        } else {
            CommonView.displayMessage("Status: Pending Response");
        }
    }

    /**
     * Prompts the user to create a new enquiry.
     *
     * @param applicantNric the NRIC of the applicant
     * @param projects the list of projects to choose from
     * @return a new Enquiry object or null if cancelled
     */
    public static Enquiry getEnquiryInput(String applicantNric, List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available for enquiry.");
            return null;
        }

        CommonView.displayHeader("Select a project for enquiry");
        for (int i = 0; i < projects.size(); i++) {
            CommonView.displayMessage(String.format("%d. %s", i + 1, projects.get(i).getProjectName()));
        }

        int projectChoice = CommonView.promptInt("\nSelect a project number (or 0 to cancel): ", 0, projects.size());
        if (projectChoice == 0) return null;

        Project selectedProject = projects.get(projectChoice - 1);
        String query = CommonView.prompt("\nEnter your enquiry: ");

        if (query.isEmpty()) {
            displayError("Enquiry cannot be empty.");
            return null;
        }

        return new Enquiry(applicantNric, selectedProject.getProjectID(), query);
    }

    /**
     * Prompts the user to select an enquiry for editing or deletion.
     *
     * @param action the action to perform ("edit" or "delete")
     * @param size the number of available enquiries
     * @return the selected enquiry index (0-based), or -1 if cancelled
     */
    public static int getEnquiryToEditOrDelete(String action, int size) {
        int choice = CommonView.promptInt(
                String.format("Enter the number of the enquiry to %s (1-%d) or 0 to cancel: ", action, size),
                0, size);
        return choice - 1;
    }

    /**
     * Prompts the user to enter updated enquiry content.
     *
     * @return the new content, or null if cancelled
     */
    public static String getUpdatedContents() {
        String content = CommonView.prompt("Enter your updated enquiry (or press Enter to cancel): ");
        return content.isEmpty() ? null : content;
    }

    /** Displays a message when there are no enquiries to show. */
    public static void displayEmptyMessage() {
        CommonView.displayMessage("No enquiries available.");
    }

    /** Displays a success message for enquiry creation. */
    public static void displayEnquiryCreatedMessage() {
        displaySuccess("Enquiry created successfully.");
    }

    /**
     * Displays a success message.
     *
     * @param message the message to display
     */
    public static void displaySuccess(String message) {
        CommonView.displaySuccess(message);
    }

    /**
     * Displays an error message.
     *
     * @param message the error message
     */
    public static void displayError(String message) {
        CommonView.displayError(message);
    }
}
