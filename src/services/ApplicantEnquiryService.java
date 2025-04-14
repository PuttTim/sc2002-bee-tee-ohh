package services;

import models.*;
import models.enums.EnquiryStatus;
import repositories.EnquiryRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicantEnquiryService {
    public static List<Enquiry> viewEnquiriesByApplicant(Applicant applicant) {
        return EnquiryRepository.getAll().stream()
                .filter(enquiry -> enquiry.getApplicantNRIC().equals(applicant.getUserNRIC()))
                .collect(Collectors.toList());
    }

    public static void submitEnquiry(Enquiry enquiry) {
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

        // Check if enquiry can be edited (e.g. not responded yet)
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
}
