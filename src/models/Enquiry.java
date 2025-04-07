package models;

import java.time.LocalDateTime;

import enums.EnquiryStatus;

public class Enquiry {
    private static int lastEnquiryId = 0;

    private String enquiryId;
    private String applicantNric;
    private String projectId;
    private String query;
    private String response;
    private EnquiryStatus enquiryStatus;
    private LocalDateTime enquiryDate;
    private LocalDateTime lastUpdated;
    private String respondedBy;

    public Enquiry(String applicantNric, String projectId, String query) {
        this.enquiryId = "E" + (++Enquiry.lastEnquiryId);
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.query = query;
        this.response = null;
        this.enquiryStatus = EnquiryStatus.PENDING;
        this.enquiryDate = LocalDateTime.now();
        this.lastUpdated = this.enquiryDate;
        this.respondedBy = null;
    }

    public Enquiry(String enquiryId, String applicantNric, String projectId, String query, String response,
                   EnquiryStatus enquiryStatus, LocalDateTime enquiryDate,
                   LocalDateTime lastUpdated, String respondedBy) {
        this.enquiryId = enquiryId;
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.query = query;
        this.response = response;
        this.enquiryStatus = enquiryStatus;
        this.enquiryDate = enquiryDate;
        this.lastUpdated = lastUpdated;
        this.respondedBy = respondedBy;

        try {
            int numericId = Integer.parseInt(enquiryId.replaceAll("\\D+", ""));
            if (numericId > Enquiry.lastEnquiryId) {
                Enquiry.lastEnquiryId = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // Getters
    public String getEnquiryId() {
        return enquiryId;
    }

    public String getApplicantNric() {
        return applicantNric;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getQuery() {
        return query;
    }

    public String getResponse() {
        return response;
    }

    public EnquiryStatus getEnquiryStatus() {
        return enquiryStatus;
    }

    public LocalDateTime getEnquiryDate() {
        return enquiryDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getRespondedBy() {
        return respondedBy;
    }

    // Setters
    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public void setApplicantNric(String applicantNric) {
        this.applicantNric = applicantNric;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setEnquiryDate(LocalDateTime enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setRespondedBy(String respondedBy) {
        this.respondedBy = respondedBy;
    }

    // Status Update Method
    public void markAsResponded(String responder, String response) {
        this.response = response;
        this.enquiryStatus = EnquiryStatus.RESPONDED;
        this.respondedBy = responder;
        this.lastUpdated = LocalDateTime.now();
    }
}