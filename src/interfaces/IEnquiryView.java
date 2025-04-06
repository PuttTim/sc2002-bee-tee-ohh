package interfaces;

import models.Enquiry;

import java.util.List;

public interface IEnquiryView {
    public void showEnquiryList(List<Enquiry> enquiries);
    public void displayEmptyMessage();
    public void displayEnquiry(Enquiry enquiry);
}
