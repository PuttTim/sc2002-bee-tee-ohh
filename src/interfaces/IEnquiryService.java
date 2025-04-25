package interfaces;

import java.util.List;

import models.Applicant;
import models.Enquiry;
import models.Project;

/**
 * Interface for managing property enquiry operations.
 */
public interface IEnquiryService {
    /**
     * Retrieves all enquiries submitted for a specific project.
     *
     * @param project The project for which to retrieve enquiries.
     * @return A list of enquiries related to the given project.
     */
    List<Enquiry> getProjectEnquiries(Project project);

    /**
     * Retrieves all enquiries made by a specific applicant.
     *
     * @param applicant The applicant whose enquiries are to be retrieved.
     * @return A list of enquiries submitted by the applicant.
     */
    List<Enquiry> getEnquiriesByApplicant(Applicant applicant);

    /**
     * Submits a new enquiry.
     *
     * @param enquiry The enquiry to create.
     */
    void createEnquiry(Enquiry enquiry);

    /**
     * Updates the content of an existing enquiry made by the applicant.
     *
     * @param applicant   The applicant requesting the edit.
     * @param enquiryId   The ID of the enquiry to edit.
     * @param newContent  The new content for the enquiry.
     */
    void editEnquiry(Applicant applicant, String enquiryId, String newContent);

    /**
     * Deletes an enquiry made by the applicant.
     *
     * @param applicant The applicant requesting the deletion.
     * @param enquiryId The ID of the enquiry to delete.
     */
    void deleteEnquiry(Applicant applicant, String enquiryId);

    /**
     * Submits a reply to an enquiry.
     *
     * @param enquiry       The enquiry being replied to.
     * @param response      The reply content.
     * @param responderNRIC The NRIC of the responder.
     * @return true if the reply was successfully submitted, false otherwise.
     */
    boolean replyToEnquiry(Enquiry enquiry, String response, String responderNRIC);

}
