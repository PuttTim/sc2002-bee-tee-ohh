package controllers;

import models.Project;
import models.Enquiry;
import repositories.EnquiryRepository;
import views.EnquiryView;
import java.util.List;

public class EnquiryController {
    public static void listEnquiries(Project project) {
        List<Enquiry> enquiries = EnquiryRepository.getEnquiriesByProject(project.getProjectID());
        EnquiryView.displayEnquiries(enquiries);
    }

    public static void createEnquiry(Project project, String applicantNric, String query) {
        Enquiry enquiry = new Enquiry(applicantNric, project.getProjectID(), query);
        EnquiryRepository.add(enquiry);
        EnquiryView.displayEnquiryCreatedMessage();
    }

    public static void respondToEnquiry(Enquiry enquiry, String response, String responderNric) {
        enquiry.markAsResponded(responderNric, response);
        EnquiryRepository.saveAll();
    }
}
