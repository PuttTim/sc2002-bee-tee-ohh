package views;

import models.Enquiry;
import interfaces.IEnquiryView;
import java.util.List;

public class EnquiryView {
    public static void displayEnquiries(List<Enquiry> enquiries) {
        if (enquiries.isEmpty()) {
            CommonView.displayMessage("No enquiries found.");
            return;
        }

        for (Enquiry enquiry : enquiries) {
            displayEnquiry(enquiry);
            CommonView.displaySeparator();
        }
    }

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

    public static String getEnquiryDetails() {
        return CommonView.prompt("\nEnter your enquiry: ");
    }

    public static void displayEnquiryCreatedMessage() {
        CommonView.displaySuccess("Enquiry created successfully.");
    }

    public static void displayEmptyMessage() {
        CommonView.displayMessage("No enquiries available.");
    }

    public static void showEnquiryList(List<Enquiry> enquiries) {
        if (enquiries.isEmpty()) {
            displayEmptyMessage();
            return;
        }

        CommonView.displayHeader("Enquiries");
        for (int i = 0; i < enquiries.size(); i++) {
            Enquiry enquiry = enquiries.get(i);
            CommonView.displayMessage(String.format("%d. From: %s", i + 1, enquiry.getApplicantNRIC()));
            CommonView.displayMessage("   Query: " + enquiry.getQuery());
            CommonView.displayMessage("   Status: " + (enquiry.isResponse() ? "Responded" : "Pending"));
            CommonView.displaySeparator();
        }
    }
}
