package models;

import java.time.LocalDate;

import enums.EnquiryStatus;

public class Enquiry {
    private String enquiryId;
    private Applicant applicantNric;
    private Project projectId;
    private String content;
    private EnquiryStatus enquiryStatus;
    private LocalDate enquiryDate;
    private String respondedBy; // Might need to connect this to officer and manager

    public Enquiry(String enquiryId, Applicant applicantNric, Project projectId, String content, String respondedBy) {
        this.enquiryId = enquiryId;
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.content = content;
        this.enquiryStatus = EnquiryStatus.PENDING;
        this.enquiryDate = LocalDate.now();
        this.respondedBy = null;
    }

    // Getters
    public String getEnquiryId() {
        return enquiryId;
    }

    public Applicant getApplicantNric() {
        return applicantNric;
    }

    public Project getProjectId() {
        return projectId;
    }

    public String getContent() {
        return content;
    }

    public EnquiryStatus getEnquiryStatus() {
        return enquiryStatus;
    }

    public LocalDate getEnquiryDate() {
        return enquiryDate;
    }

    public String getRespondedBy() {
        return respondedBy;
    }

    // Setters
    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public void setApplicantNric(Applicant applicantNric) {
        this.applicantNric = applicantNric;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void markAsResponded(String responder) {
        this.enquiryStatus = EnquiryStatus.RESPONDED;
        this.respondedBy = responder;
    }

    public void setEnquiryDate(LocalDate enquiryDate) {
        this.enquiryDate = enquiryDate;
    }
}