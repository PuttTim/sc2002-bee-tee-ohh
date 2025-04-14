package services;

import models.Enquiry;
import models.Project;
import repositories.EnquiryRepository;
import java.util.List;

public class EnquiryService {
    public EnquiryService(EnquiryRepository enquiryRepository) {
    }

    public List<Enquiry> getEnquiriesByProject(Project project) {
        return EnquiryRepository.getEnquiriesByProject(project.getProjectID());
    }

    public void createEnquiry(Enquiry enquiry) {
        EnquiryRepository.add(enquiry);
    }
}
