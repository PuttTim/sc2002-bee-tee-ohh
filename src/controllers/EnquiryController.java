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

public class EnquiryController {
        public static void viewApplicantEnquiries(Applicant applicant) {
        List<Enquiry> enquiries = EnquiryService.getEnquiriesByApplicant(applicant);
        EnquiryView.displayEnquiryList(enquiries);
        EnquiryView.showEnquiryMenu(applicant);
    }

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

    public static void viewProjectEnquiries(Project project) {
        List<Enquiry> enquiries = EnquiryService.getProjectEnquiries(project);
        EnquiryView.displayEnquiryList(enquiries);
    }

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
