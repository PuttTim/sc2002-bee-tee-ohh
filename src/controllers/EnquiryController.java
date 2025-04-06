package controllers;

import models.Project;
import models.Enquiry;
import repositories.EnquiryRepository;
import views.EnquiryView;
import java.util.List;

public class EnquiryController {
    private final EnquiryView enquiryView;

    public EnquiryController() {
        this.enquiryView = new EnquiryView();
    }

    public void listEnquiries(Project project) {
        List<Enquiry> enquiries = EnquiryRepository.getEnquiriesByProject(project.getProjectId());
        enquiryView.displayEnquiries(enquiries);
    }

    public void createEnquiry(Project project, String applicantNric, String query) {
        String enquiryId = "E" + (EnquiryRepository.getAll().size() + 1);
        Enquiry enquiry = new Enquiry(enquiryId, applicantNric, project.getProjectId(), query, null);
        EnquiryRepository.add(enquiry);
        enquiryView.displayEnquiryCreatedMessage();
    }

    public void respondToEnquiry(Enquiry enquiry, String response, String responderNric) {
        enquiry.markAsResponded(responderNric, response);
        EnquiryRepository.saveAll();
        // enquiryView.displayEnquiryRespondedMessage();
    }
}
