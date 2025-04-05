package views;

import models.Enquiry;
import models.Project;
import services.ApplicantEnquiryService;

import java.util.List;
import java.util.Scanner;

public class ApplicantEnquiryView {
    private Scanner scanner = new Scanner(System.in);
    private ApplicantEnquiryService enquiryService;

    //constructor
    public ApplicantEnquiryView(ApplicantEnquiryService service) {
        this.enquiryService = service;
    }

    //the menu for showing enquiry options
    public void showEnquiryMenu() { //main menu for enquiries
        System.out.println("\n===== Enquiry Menu =====");
        System.out.println("1. View Enquiries");
        System.out.println("2. Submit Enquiry");
        System.out.println("3. Edit Enquiry");
        System.out.println("4. Delete Enquiry");
        System.out.println("5. Exit Enquiry Menu");
        System.out.print("Select an option: ");
    }

    //display submitted enquiries
    public void displayEnquiries(List<Enquiry> enquiries) {
        if (enquiries.isEmpty()) { //no enquiries submitted
            System.out.println("You have not submitted any enquiries.");
        } else {
            System.out.println("Your Enquiries:");
            for (int i = 0; i < enquiries.size(); i++) {
                Enquiry e = enquiries.get(i);
                System.out.printf("%d. [%s] %s\n", i + 1, e.getProject().getProjectId(), e.getEnquiry());
                //print out each enquiry (project name, id, enquiry)
            }
        }
    }

    //get applicant input for submitting enquiry
    public Enquiry getEnquiryInput(String applicantNric, List<Project> projects) {
        System.out.println("Select the project ID to enquire about:"); //user input
        for (int i = 0; i < projects.size(); i++) {
            //display each project by id and name for applicant to choose from
            Project project = projects.get(i);
            System.out.printf("%d. %s - %s\n", i + 1, project.getProjectId(), project.getProjectName());
        }

        System.out.print("Enter project ID: "); //user input
        String projectId = scanner.nextLine();

        //check projectId for validity
        Project selectedProject = null;
        for (Project project : projects) {
            if (project.getProjectId().equals(projectId)) {
                selectedProject = project; //assign the matching project (by id) to selectedProject
                break;
            }
        }

        if (selectedProject == null) {
            System.out.println("Invalid project ID."); //handling invalid input
            return null;
        }

        System.out.print("Enter your enquiry message: "); //user input
        String content = scanner.nextLine();

        return new Enquiry(null, null, selectedProject, content); //return full enquiry
    }

    //for editing or deleting enquiries
    public int getEnquiryToEditOrDelete(String action, int size) {
        System.out.printf("Enter the enquiry number to %s (1 to %d): ", action, size);
        //action: edit or delete
        return scanner.nextInt() - 1; //for enquiry list index
    }

    //get updated enquiry contents
    public String getUpdatedContents() {
        scanner.nextLine(); // clear buffer
        System.out.print("Enter your new message: "); //user input
        return scanner.nextLine();
    }

    //successful enquiry
    public void showSuccess(String msg) {
        System.out.println("Successful enquiry: " + msg); //display success
    }

    //error encountered
    public void showError(String msg) {
        System.out.println("Error occurred when submitting enquiry: " + msg); //display error
    }
}
