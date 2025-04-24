package services;

import java.util.ArrayList;
import java.util.List;

import models.Applicant;
import models.Enquiry;
import models.Project;
import models.enums.EnquiryStatus;

import repositories.EnquiryRepository;
import views.CommonView;

/**
 * Service class for managing property enquiry operations.
 * <p>
 * Provides functionality for creating, retrieving, editing, and responding to property enquiries.
 * Enforces business rules regarding enquiry ownership and status transitions.
 * </p>
 */
public class EnquiryService {

    /**
     * Retrieves all enquiries for a specific project.
     *
     * @param project the project to filter enquiries by
     * @return list of enquiries associated with the given project
     */
    public static List<Enquiry> getProjectEnquiries(Project project) {
        return EnquiryRepository.getEnquiriesByProject(project.getProjectID());
    }

    /**
     * Retrieves all enquiries submitted by a specific applicant.
     * <p>
     * Filters enquiries based on the applicant's NRIC.
     * </p>
     *
     * @param applicant the applicant whose enquiries to retrieve
     * @return list of enquiries submitted by the applicant
     */
    public static List<Enquiry> getEnquiriesByApplicant(Applicant applicant) {
        List<Enquiry> enquiries = EnquiryRepository.getAll();
        List<Enquiry> filteredEnquiries = new ArrayList<Enquiry>();

        for (Enquiry enquiry : enquiries) {
            if (enquiry.getApplicantNRIC().equals(applicant.getUserNRIC())) {
                filteredEnquiries.add(enquiry);
            }
        }

        return filteredEnquiries;
    }

    /**
     * Creates a new enquiry.
     * <p>
     * Validates the enquiry object before persisting it.
     * </p>
     *
     * @param enquiry the enquiry to create
     * @throws IllegalArgumentException if enquiry is null
     */
    public static void createEnquiry(Enquiry enquiry) {
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry cannot be null");
        }
        EnquiryRepository.add(enquiry);
        EnquiryRepository.saveAll();
    }

    /**
     * Edits an existing enquiry's content.
     * <p>
     * Validates that:
     * <ul>
     *   <li>The enquiry exists</li>
     *   <li>The applicant owns the enquiry</li>
     *   <li>The enquiry hasn't been responded to</li>
     *   <li>The new content is valid</li>
     * </ul>
     * </p>
     *
     * @param applicant the applicant editing the enquiry
     * @param enquiryId ID of the enquiry to edit
     * @param newContent the updated enquiry content
     * @throws IllegalArgumentException if validation fails
     */
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

    /**
     * Deletes an existing enquiry.
     * <p>
     * Validates that:
     * <ul>
     *   <li>The enquiry exists</li>
     *   <li>The applicant owns the enquiry</li>
     * </ul>
     * </p>
     *
     * @param applicant the applicant deleting the enquiry
     * @param enquiryId ID of the enquiry to delete
     * @throws IllegalArgumentException if validation fails
     */
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

    /**
     * Adds a response to an enquiry and marks it as responded.
     * <p>
     * Validates that:
     * <ul>
     *   <li>The enquiry exists</li>
     *   <li>The response content is valid</li>
     *   <li>The enquiry hasn't already been responded to</li>
     * </ul>
     * </p>
     *
     * @param enquiry the enquiry to respond to
     * @param response the response content
     * @param responderNRIC the NRIC of the staff member responding
     * @return {@code true} if response was successful, {@code false} otherwise
     */
    public static boolean replyToEnquiry(Enquiry enquiry, String response, String responderNRIC) {
        if (enquiry == null) {
            return false;
        }

        if (response == null || response.trim().isEmpty()) {
            CommonView.displayError("Response cannot be empty");
            return false;
        }

        if (enquiry.getEnquiryStatus() == EnquiryStatus.RESPONDED) {
            CommonView.displayError("Enquiry has already been responded to");
            return false;
        }

        enquiry.markAsResponded(responderNRIC, response);
        EnquiryRepository.saveAll();
        return true;
    }
}