package controllers;

import models.Project;
import models.Enquiry;
import repositories.EnquiryRepository;
import views.EnquiryView;
import java.util.List;

public class EnquiryController {
    private final EnquiryView enquiryView;

    public EnquiryController(EnquiryView enquiryView) {
        this.enquiryView = enquiryView;
    }

    public void listEnquiries(Project project) {
        List<Enquiry> enquiries = EnquiryRepository.getEnquiriesByProject(project.getProjectId());
        enquiryView.displayEnquiries(enquiries);
    }

    public void createEnquiry(Enquiry enquiry) {
        EnquiryRepository.add(enquiry);
        // enquiryView.displayEnquiryCreatedMessage();
    }

    public void respondToEnquiry(Enquiry enquiry, String response, String responderNric) {
        enquiry.markAsResponded(responderNric, response);
        EnquiryRepository.saveAll();
        // enquiryView.displayEnquiryRespondedMessage();
    }
}
