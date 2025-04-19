package services;

import models.Applicant;
import models.Enquiry;
import models.Project;
import models.enums.EnquiryStatus;
import repositories.EnquiryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handles actions related to enquiries, such as:
 * <ul>
 *     <li>Creating, editing, deleting, responding to enquiries made by applicants</li>
 * </ul>
 */
public class EnquiryService {

    /**
     * Gets all enquiries made about a specific project.
     *
     * @param project the project to filter enquiries by
     * @return a list of enquiries about to the given project
     */
    public static List<Enquiry> getProjectEnquiries(Project project) {
        return EnquiryRepository.getEnquiriesByProject(project.getProjectID());
    }

    /**
     * Gets all enquiries submitted by a specific applicant.
     *
     * @param applicant the applicant whose enquiries are to be retrieved
     * @return a list of enquiries submitted by the applicant
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
     * Creates a new enquiry and saves it.
     *
     * @param enquiry the enquiry to be created
     * @throws IllegalArgumentException if the enquiry is <code>null</code>
     */
    public static void createEnquiry(Enquiry enquiry) {
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry cannot be null");
        }
        EnquiryRepository.add(enquiry);
        EnquiryRepository.saveAll();
    }

    /**
     * Allows an applicant to edit the content of their enquiry.
     *
     * @param applicant the applicant making the edit
     * @param enquiryId the ID of the enquiry to be edited
     * @param newContent the new enquiry content
     * @throws IllegalArgumentException if the enquiry or content is invalid
     * @throws IllegalStateException if the enquiry is not owned by the applicant or already responded
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
     * Deletes an enquiry made by the applicant.
     *
     * @param applicant the applicant requesting deletion
     * @param enquiryId the ID of the enquiry to delete
     * @throws IllegalArgumentException if the enquiry is not found
     * @throws IllegalStateException if the enquiry does not belong to the applicant
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
     * Allows an officer to respond to an enquiry.
     *
     * @param enquiry the enquiry being responded to
     * @param response the response message
     * @param responderNRIC the NRIC of the officer responding
     * @throws IllegalArgumentException if the enquiry or response is invalid
     * @throws IllegalStateException if the enquiry has already been responded to
     */
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