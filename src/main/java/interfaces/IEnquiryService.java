package interfaces;

import java.util.List;

import models.Applicant;
import models.Enquiry;
import models.Project;

/**
 * Interface for managing property enquiry operations.
 */
public interface IEnquiryService {
    List<Enquiry> getProjectEnquiries(Project project);
    List<Enquiry> getEnquiriesByApplicant(Applicant applicant);
    void createEnquiry(Enquiry enquiry);
    void editEnquiry(Applicant applicant, String enquiryId, String newContent);
    void deleteEnquiry(Applicant applicant, String enquiryId);
    boolean replyToEnquiry(Enquiry enquiry, String response, String responderNRIC);
}
