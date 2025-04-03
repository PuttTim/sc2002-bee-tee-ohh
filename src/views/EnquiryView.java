package views;

import models.Enquiry;
import java.util.List;
import java.util.Scanner;

public class EnquiryView {
    private final Scanner scanner;

    public EnquiryView() {
        this.scanner = new Scanner(System.in);
    }

    public void displayEnquiries(List<Enquiry> enquiries) {
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        System.out.println("List of Enquiries:");
        for (Enquiry enquiry : enquiries) {
            System.out.println("----------------------------------");
            System.out.println("Enquiry ID: " + enquiry.getEnquiryId());
            System.out.println("Applicant NRIC: " + enquiry.getApplicantNric());
            System.out.println("Project ID: " + enquiry.getProjectId());
            System.out.println("Query: " + enquiry.getQuery());
            System.out.println("Response: " + (enquiry.getResponse() != null ? enquiry.getResponse() : "Not yet responded"));
            System.out.println("Status: " + enquiry.getEnquiryStatus());
            System.out.println("Enquiry Date: " + enquiry.getEnquiryDate());
            System.out.println("Responded By: " + (enquiry.getRespondedBy() != null ? enquiry.getRespondedBy() : "Not yet responded"));
            System.out.println("----------------------------------");
        }
    }
}
