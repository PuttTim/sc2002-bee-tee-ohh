package controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import models.Applicant;
import models.Project;
import models.Enquiry;
import models.Manager;
import models.Officer;

import repositories.ProjectRepository;
import services.EnquiryService;

import views.CommonView;
import views.EnquiryView;

/**
 * Controller class for managing applicant and project enquiries.
 * <p>
 * Managing includes operations like:
 * <ul>
 *   <li>Viewing applicant and project enquiries</li>
 *   <li>Creating, editing, and deleting enquiries</li>
 *   <li>Managing replies to enquiries by officers and managers</li>
 * </ul>
 */
public class EnquiryController {

    /**
     * Displays a list of enquiries submitted by a specific applicant and shows the enquiry menu.
     *
     * @param applicant the applicant whose enquiries are to be displayed
     */
    public static void viewApplicantEnquiries(Applicant applicant) {
        List<Enquiry> enquiries = EnquiryService.getEnquiriesByApplicant(applicant);
        EnquiryView.displayEnquiryList(enquiries);
        EnquiryView.showEnquiryMenu(applicant);
    }

    /**
     * Allows an applicant to create a new enquiry for a visible project.
     * <p>
     * Creation process:
     * <ul>
     *   <li>Displays all visible projects</li>
     *   <li>Collects enquiry input from the applicant</li>
     *   <li>Saves the enquiry using EnquiryService</li>
     * </ul>
     *
     * @param applicant the applicant who is submitting the enquiry
     */
    public static void createNewEnquiry(Applicant applicant) {
        List<Project> availableProjects = ProjectRepository.getAll().stream()
                .filter(Project::isVisible)
                .collect(Collectors.toList());

        Enquiry enquiry = EnquiryView.getEnquiryInput(applicant.getUserNRIC(), availableProjects);
        if (enquiry != null) {
            EnquiryService.createEnquiry(enquiry);
            EnquiryView.displayEnquiryCreatedMessage();
        }
    }

    /**
     * Allows an applicant to edit one of their existing enquiries.
     * <p>
     * Editing process:
     * <ul>
     *   <li>Displays existing enquiries</li>
     *   <li>Prompts the user to select one enquiry to edit</li>
     *   <li>Updates the enquiry content if a valid input is provided</li>
     * </ul>
     *
     * @param applicant the applicant editing the enquiry
     */
    public static void editEnquiry(Applicant applicant) {
        List<Enquiry> existingEnquiries = EnquiryService.getEnquiriesByApplicant(applicant);
        if (existingEnquiries.isEmpty()) {
            EnquiryView.displayError("No enquiries available to edit");
            return;
        }

        EnquiryView.displayEnquiryList(existingEnquiries);
        int editIndex = EnquiryView.getEnquiryToEditOrDelete("edit", existingEnquiries.size());

        if (editIndex >= 0 && editIndex < existingEnquiries.size()) {
            String newContent = EnquiryView.getUpdatedContents();
            if (newContent != null) {
                EnquiryService.editEnquiry(applicant, existingEnquiries.get(editIndex).getEnquiryID(), newContent);
                EnquiryView.displaySuccess("Enquiry updated successfully");
                EnquiryView.showEnquiryMenu(applicant);
            }
        } else {
            EnquiryView.displayError("Invalid selection");
        }
    }

    /**
     * Allows an applicant to delete one of their enquiries.
     * <p>
     * Deletion process:
     * <ul>
     *   <li>Displays list of enquiries</li>
     *   <li>Prompts the applicant to choose which enquiry to delete</li>
     *   <li>Deletes the selected enquiry if valid</li>
     * </ul>
     *
     * @param applicant the applicant deleting their enquiry
     */
    public static void deleteEnquiry(Applicant applicant) {
        List<Enquiry> enquiriesToDelete = EnquiryService.getEnquiriesByApplicant(applicant);
        if (enquiriesToDelete.isEmpty()) {
            EnquiryView.displayError("No enquiries available to delete");
            return;
        }

        EnquiryView.displayEnquiryList(enquiriesToDelete);
        int deleteIndex = EnquiryView.getEnquiryToEditOrDelete("delete", enquiriesToDelete.size());

        if (deleteIndex >= 0 && deleteIndex < enquiriesToDelete.size()) {
            EnquiryService.deleteEnquiry(applicant, enquiriesToDelete.get(deleteIndex).getEnquiryID());
            EnquiryView.displaySuccess("Enquiry deleted successfully");
        } else {
            EnquiryView.displayError("Invalid selection");
        }
    }

    /**
     * Displays all enquiries made for a specific project.
     *
     * @param project the project which enquiries are to be viewed
     */
    public static void viewProjectEnquiries(Project project) {
        List<Enquiry> enquiries = EnquiryService.getProjectEnquiries(project);
        EnquiryView.displayEnquiryList(enquiries);
    }

    /**
     * Allows an officer or manager to manage enquiries for a project.
     * <p>
     * Managing includes these actions:
     * <ul>
     *   <li>View the list of enquiries</li>
     *   <li>See full details of a selected enquiry</li>
     *   <li>Submit a reply to the enquiry</li>
     * </ul>
     *
     * @param officer optional officer managing the enquiry
     * @param manager optional manager managing the enquiry
     * @param project the project associated with the enquiries
     */
    public static void manageProjectEnquiries(Optional<Officer> officer, Optional<Manager> manager, Project project) {
        String nric = officer.map(Officer::getUserNRIC).orElse(manager.map(Manager::getUserNRIC).orElse(null));

        List<Enquiry> enquiries = EnquiryService.getProjectEnquiries(project);
        if (enquiries.isEmpty()) {
            EnquiryView.displayEmptyMessage();
            return;
        }

        EnquiryView.displayEnquiryList(enquiries);
        int choice = CommonView.promptInt("Enter the number of the enquiry to view details/reply (or 0 to cancel): ", 0, enquiries.size());

        if (choice == 0) {
            return;
        }

        if (choice < 1 || choice > enquiries.size()) {
            EnquiryView.displayError("Invalid enquiry number selected");
            return;
        }

        Enquiry selectedEnquiry = enquiries.get(choice - 1);
        EnquiryView.displayEnquiry(selectedEnquiry);

        if (CommonView.promptYesNo("Do you want to reply to this enquiry?")) {
            String reply = CommonView.prompt("Enter your reply: ");
            if (EnquiryService.replyToEnquiry(selectedEnquiry, reply, nric)) {
                EnquiryView.displaySuccess("Reply submitted successfully");
            } else {
                EnquiryView.displayError("Failed to submit reply. Please try again.");
            }
        }
    }
}
