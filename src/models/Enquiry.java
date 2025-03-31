package models;

public class Enquiry {
    private String enquiryID;
    private Applicant applicant;
    private Project project;
    private String enquiryContent;

    //constructor
    public Enquiry(String enquiryID, Applicant applicant, Project project, String enquiryContent) {
        this.enquiryID = enquiryID;
        this.applicant = applicant;
        this.project = project;
        this.enquiryContent = enquiryContent;
    }

    //getters
    public String getEnquiryID() {
        return enquiryID;
    }
    public Applicant getApplicant() {
        return applicant;
    }
    public Project getProject() {
        return project;
    }
    public String getEnquiry() {
        return enquiryContent;
    }

    //setters
    public void setEnquiryID(String enquiryID) {
        this.enquiryID = enquiryID;
    }
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public void setEnquiry(String enquiryContent) {
        this.enquiryContent = enquiryContent;
    }
}
