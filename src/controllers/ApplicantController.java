package controllers;

import models.*;
import services.*;
import views.*;

import java.util.List;
import java.util.Scanner;

public class ApplicantController {
    private ApplicantApplicationService applicationService;
    private ApplicantEnquiryService enquiryService;
    private ApplicantApplicationView applicationView;
    private ApplicantEnquiryView enquiryView;
    private ApplicantController applicantController;
    private Scanner scanner;

    //constructor
    public ApplicantController(ApplicantApplicationService applicationService,
                               ApplicantEnquiryService enquiryService,
                               ApplicantApplicationView applicationView,
                               ApplicantEnquiryView enquiryView,
                               ApplicantController applicantController) {
        this.applicationService = applicationService;
        this.enquiryService = enquiryService;
        this.applicationView = applicationView;
        this.enquiryView = enquiryView;
        this.applicantController = applicantController;
        this.scanner = new Scanner(System.in);
    }

    //start the main menu
    public void start(Applicant applicant, List<Project> allProjects) {
        while (true) {
            System.out.println("\n===== Applicant Main Menu =====");
            System.out.println("1. Application Services");
            System.out.println("2. Enquiry Services");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    //call the method
                    applicationView.showApplicationMenu(applicant, allProjects, this);
                    break;
                case 2:
                    //call the method in enquiryView
                    enquiryView.showEnquiryMenu();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //handle enquiry menu
    public void showEnquiryMenu(Applicant applicant, List<Project> allProjects) {
        while (true) {
            enquiryView.showEnquiryMenu();  //display the menu from views
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    //to view enquiries
                    List<Enquiry> enquiries = enquiryService.viewEnquiriesByApplicant(applicant);
                    enquiryView.displayEnquiries(enquiries);
                    break;

                case 2:
                    //for applicant to submit enquiry
                    Enquiry enquiryInput = enquiryView.getEnquiryInput(applicant.getNric(), allProjects); //call on views
                    if (enquiryInput != null) { //if input is done correctly
                        enquiryService.submitEnquiry(applicant, enquiryInput.getProject(), enquiryInput.getEnquiry()); //call services
                        enquiryView.showSuccess("Your enquiry has been submitted.");
                    } else {
                        enquiryView.showError("Failed to submit enquiry. Please check your input.");
                    }
                    break;

                case 3:
                    //for applicant to edit enquiry
                    List<Enquiry> existingEnquiries = enquiryService.viewEnquiriesByApplicant(applicant);
                    if (!existingEnquiries.isEmpty()) {
                        int enquiryIndex = enquiryView.getEnquiryToEditOrDelete("edit", existingEnquiries.size());
                        if (enquiryIndex >= 0 && enquiryIndex < existingEnquiries.size()) {
                            Enquiry enquiryToEdit = existingEnquiries.get(enquiryIndex);

                            //get the updated content to edit enquiry with
                            String updatedContent = enquiryView.getUpdatedContents();

                            //edit enquiry by passing the updated content
                            enquiryService.editEnquiry(applicant, enquiryToEdit.getEnquiryID(), updatedContent);
                            enquiryView.showSuccess("Enquiry updated successfully.");
                        } else {
                            enquiryView.showError("Invalid enquiry selection.");
                        }
                    } else {
                        enquiryView.showError("No enquiries available to edit.");
                    }
                    break;

                case 4:
                    //for applicant to delete enquiry
                    //get a list of enquiries that can be deleted
                    List<Enquiry> deleteList = enquiryService.viewEnquiriesByApplicant(applicant);

                    if (!deleteList.isEmpty()) { //if enquiries available
                        enquiryView.displayEnquiries(deleteList); //display the enquiries

                        //get the action (delete), method returns the index of enquiry in list
                        int index = enquiryView.getEnquiryToEditOrDelete("delete", deleteList.size());

                        if (index >= 0 && index < deleteList.size()) {
                            Enquiry selected = deleteList.get(index);
                            enquiryService.deleteEnquiry(applicant, selected.getEnquiryID()); //call on services
                            enquiryView.showSuccess("Enquiry deleted successfully.");
                        } else {
                            enquiryView.showError("Invalid selection.");
                        }
                    } else {
                        enquiryView.showError("No enquiries to delete.");
                    }
                    break;

                case 5:
                    return; //go back to start, which called this method
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
