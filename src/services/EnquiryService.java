package services;

import java.util.List;

import interfaces.IEnquiryService;
import models.Applicant;
import models.Enquiry;
import models.Project;
import models.enums.EnquiryStatus;

import repositories.EnquiryRepository;
import views.CommonView;

/**
 * Service class for managing property enquiry operations.
 * Provides functionality for creating, retrieving, editing, and responding to property enquiries.
 * Enforces business rules regarding enquiry ownership and status transitions.
 */
public class EnquiryService implements IEnquiryService {
    private static EnquiryService instance;
    
    private EnquiryService() {}

    /**
     * Returns the singleton instance of the EnquiryService class.
     * If an instance of EnquiryService does not already exist, it creates a new instance.
     *
     * @return The singleton instance of EnquiryService.
     */
    public static EnquiryService getInstance() {
        if (instance == null) {
            instance = new EnquiryService();
        }
        return instance;
    }

    /**
     * Retrieves all enquiries for a specific project.
     *
     * @param project the project to filter enquiries by
     * @return list of enquiries associated with the given project
     */
    @Override
    public List<Enquiry> getProjectEnquiries(Project project) {
        return EnquiryRepository.getEnquiriesByProject(project.getProjectID());
    }

    /**
     * Retrieves all enquiries submitted by a specific applicant.
     * Filters enquiries based on the applicant's NRIC.
     *
     * @param applicant the applicant whose enquiries to retrieve
     * @return list of enquiries submitted by the applicant
     */
    @Override
    public List<Enquiry> getEnquiriesByApplicant(Applicant applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }
        return EnquiryRepository.getAll().stream()
                .filter(enquiry -> enquiry.getApplicantNRIC().equals(applicant.getUserNRIC()))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Creates a new enquiry.
     * Validates the enquiry object.
     *
     * @param enquiry the enquiry to create
     */
    @Override
    public void createEnquiry(Enquiry enquiry) {
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry cannot be null");
        }
        if (enquiry.getQuery() == null || enquiry.getQuery().trim().isEmpty()) {
            throw new IllegalArgumentException("Enquiry content cannot be empty");
        }
        EnquiryRepository.add(enquiry);
        EnquiryRepository.saveAll();
    }

    /**
     * Edits an existing enquiry.
     * Only the owner can edit their enquiry, and only if it hasn't been responded to.
     *
     * @param applicant the applicant attempting to edit
     * @param enquiryId the ID of the enquiry to edit
     * @param newContent the new content for the enquiry
     */
    @Override
    public void editEnquiry(Applicant applicant, String enquiryId, String newContent) {
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

    /**
     * Deletes an existing enquiry.
     * Only the owner can delete their enquiry.
     *
     * @param applicant the applicant attempting to delete
     * @param enquiryId the ID of the enquiry to delete
     */
    @Override
    public void deleteEnquiry(Applicant applicant, String enquiryId) {
        Enquiry enquiry = EnquiryRepository.getEnquiryById(enquiryId);
        if (enquiry == null) {
            throw new IllegalStateException("Enquiry not found");
        }

        if (!enquiry.getApplicantNRIC().equals(applicant.getUserNRIC())) {
            throw new IllegalStateException("You can only delete your own enquiries");
        }

        EnquiryRepository.delete(enquiry);
        EnquiryRepository.saveAll();
    }

    /**
     * Adds a response to an enquiry and marks it as responded.
     * <p>
     * Several validations are performed:
     * <ul>
     *   <li>The enquiry must exist</li>
     *   <li>The response content is valid</li>
     *   <li>The enquiry hasn't already been responded to</li>
     * </ul>
     *
     * @param enquiry the enquiry to respond to
     * @param response the response content
     * @param responderNRIC the NRIC of the staff member responding
     * @return {@code true} if response was successful, {@code false} otherwise
     */
    @Override
    public boolean replyToEnquiry(Enquiry enquiry, String response, String responderNRIC) {
        if (enquiry == null || response == null || response.trim().isEmpty() || responderNRIC == null || responderNRIC.trim().isEmpty()) {
            return false;
        }

        if (enquiry.getEnquiryStatus() == EnquiryStatus.RESPONDED) {
            CommonView.displayError("Enquiry has already been responded to.");
        }

        try {
            enquiry.markAsResponded(responderNRIC, response.trim());
            EnquiryRepository.saveAll();
            return true;
        } catch (Exception e) {
            System.err.println("Error responding to enquiry: " + e.getMessage());
            return false;
        }
    }
}