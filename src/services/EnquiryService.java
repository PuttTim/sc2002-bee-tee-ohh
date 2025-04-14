package services;

import models.Enquiry;
import models.Project;
import repositories.EnquiryRepository;
import java.util.List;

public class EnquiryService {
    public static List<Enquiry> getEnquiriesByProject(Project project) {
        return EnquiryRepository.getEnquiriesByProject(project.getProjectID());
    }

    public static void createEnquiry(Enquiry enquiry) {
        EnquiryRepository.add(enquiry);
    }

    public static void replyToEnquiry(Enquiry enquiry, String response, String responderNRIC) {
        enquiry.markAsResponded(responderNRIC, response);
        EnquiryRepository.saveAll();
    }
}
