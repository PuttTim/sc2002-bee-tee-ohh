package services;

import models.*;

import java.util.ArrayList;
import java.util.List;

public class ApplicantEnquiryService {
    private static int enquiryIdCount = 1; //for generating enquiry id????
    private List<Enquiry> enquiries = new ArrayList<>(); //list of all enquiries

    private String generateEnquiryId() {
        return "E" + enquiryIdCount++; //E1, E2, E3, etc
    }

    //enquiries section
    //view the enquiries by applicant
    public List<Enquiry> viewEnquiriesByApplicant(Applicant applicant) {
        List<Enquiry> applicantEnquiries = new ArrayList<>();

        //loop through list, filter by nric
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getApplicant().getNric().equals(applicant.getNric())) { //if equal
                applicantEnquiries.add(enquiry); //add enquiry to display
            }
        }
        return applicantEnquiries;
    }

    //submit enquiries
    public void submitEnquiry(Applicant applicant, Project project, String enquiryContent) {
        String enquiryId = generateEnquiryId();
        Enquiry enquiry = new Enquiry(enquiryId, applicant, project, enquiryContent);
        enquiries.add(enquiry);
        System.out.println("Enquiry submitted.");
    }

    //view enquiries
    public List<Enquiry> viewEnquiries(Applicant applicant) {
        List<Enquiry> applicantEnquiries = new ArrayList<>();
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getApplicant().equals(applicant)) {
                applicantEnquiries.add(enquiry);
            }
        }
        return applicantEnquiries;
    }

    //edit enquiry
    public void editEnquiry(Applicant applicant, String enquiryId, String editedEnquiryContent) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getApplicant().equals(applicant) && enquiry.getEnquiryID().equals(enquiryId)) {
                enquiry.setEnquiry(editedEnquiryContent);
                System.out.println("Enquiry edited.");
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }

    //delete enquiry
    public void deleteEnquiry(Applicant applicant, String enquiryId) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getApplicant().equals(applicant) && enquiry.getEnquiryID().equals(enquiryId)) {
                enquiries.remove(enquiry);
                System.out.println("Enquiry deleted.");
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }
}
