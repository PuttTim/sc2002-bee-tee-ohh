package services;

import models.Applicant;
import models.Enquiry;
import models.Project;
import models.enums.EnquiryStatus;
import repositories.EnquiryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnquiryService {
    public static List<Enquiry> getProjectEnquiries(Project project) {
        return EnquiryRepository.getEnquiriesByProject(project.getProjectID());
    }

    public static List<Enquiry> getEnquiriesByApplicant(Applicant applicant) {
        List<Enquiry> enquiries = EnquiryRepository.getAll();

        List<Enquiry> filteredEnquiries = new ArrayList<Enquiry>();

        for (Enquiry enquiry : enquiries) {
            System.out.println("Enquiry: " + enquiry.getEnquiryID() + ", Applicant NRIC: " + enquiry.getApplicantNRIC() + ", Applicant NRIC: " + applicant.getUserNRIC());
            if (enquiry.getApplicantNRIC().equals(applicant.getUserNRIC())) {
                filteredEnquiries.add(enquiry);
            }
        }

        return filteredEnquiries;

        // return EnquiryRepository.getAll().stream()
        //         .filter(enquiry -> enquiry.getApplicantNRIC().equals(applicant.getUserNRIC()))
        //         .collect(Collectors.toList());
    }

    public static void createEnquiry(Enquiry enquiry) {
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry cannot be null");
        }
        EnquiryRepository.add(enquiry);
        EnquiryRepository.saveAll();
    }

    public static void editEnquiry(Applicant applicant, String enquiryId, String newContent) {
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("New enquiry content cannot be empty");
        }

        Enquiry enquiry = EnquiryRepository.getEnquiryById(enquiryId);
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry not found");
        }

        if (!enquiry.getApplicantNRIC().equals(applicant.getUserNRIC())) {
            throw new IllegalStateException("You can only edit your own enquiries");
        }

        if (enquiry.getEnquiryStatus() == EnquiryStatus.RESPONDED) {
            throw new IllegalStateException("Cannot edit an enquiry that has been responded to");
        }

        enquiry.setQuery(newContent.trim());
        EnquiryRepository.saveAll();
    }

    public static void deleteEnquiry(Applicant applicant, String enquiryId) {
        Enquiry enquiry = EnquiryRepository.getEnquiryById(enquiryId);
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry not found");
        }

        if (!enquiry.getApplicantNRIC().equals(applicant.getUserNRIC())) {
            throw new IllegalStateException("You can only delete your own enquiries");
        }

        EnquiryRepository.delete(enquiry);
        EnquiryRepository.saveAll();
    }

    public static void replyToEnquiry(Enquiry enquiry, String response, String responderNRIC) {
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry cannot be null");
        }
        
        if (response == null || response.trim().isEmpty()) {
            throw new IllegalArgumentException("Response cannot be empty");
        }

        if (enquiry.getEnquiryStatus() == EnquiryStatus.RESPONDED) {
            throw new IllegalStateException("Enquiry has already been responded to");
        }

        enquiry.markAsResponded(responderNRIC, response);
        EnquiryRepository.saveAll();
    }
}
