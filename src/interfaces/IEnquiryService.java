package interfaces;

import models.Enquiry;
import models.Project;

import java.util.List;

public interface IEnquiryService {
    public List<Enquiry> getProjectEnquiries(Project project);
    public void replyToEnquiry(Enquiry enquiry);
}
