package models;

import java.time.LocalDateTime;

import models.enums.EnquiryStatus;

public class Enquiry {
    private static int lastEnquiryID = 0;

    private String enquiryID;
    private String applicantNRIC;
    private String projectID;
    private String query;
    private String response;
    private EnquiryStatus enquiryStatus;
    private LocalDateTime enquiryDate;
    private LocalDateTime lastUpdated;
    private String respondedBy;

    public Enquiry(String applicantNRIC, String projectID, String query) {
        this.enquiryID = "E" + (++Enquiry.lastEnquiryID);
        this.applicantNRIC = applicantNRIC;
        this.projectID = projectID;
        this.query = query;
        this.response = null;
        this.enquiryStatus = EnquiryStatus.PENDING;
        this.enquiryDate = LocalDateTime.now();
        this.lastUpdated = this.enquiryDate;
        this.respondedBy = null;
    }

    public Enquiry(String enquiryID, String projectID, String applicantNRIC, String query) {
        this.enquiryID = enquiryID;
        this.projectID = projectID;
        this.applicantNRIC = applicantNRIC;
        this.query = query;
        this.response = null;
        this.respondedBy = null;
    }

    public Enquiry(String enquiryID, String applicantNRIC, String projectID, String query, String response,
                   EnquiryStatus enquiryStatus, LocalDateTime enquiryDate,
                   LocalDateTime lastUpdated, String respondedBy) {
        this.enquiryID = enquiryID;
        this.applicantNRIC = applicantNRIC;
        this.projectID = projectID;
        this.query = query;
        this.response = response;
        this.enquiryStatus = enquiryStatus;
        this.enquiryDate = enquiryDate;
        this.lastUpdated = lastUpdated;
        this.respondedBy = respondedBy;

        try {
            int numericId = Integer.parseInt(enquiryID.replaceAll("\\D+", ""));
            if (numericId > Enquiry.lastEnquiryID) {
                Enquiry.lastEnquiryID = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // Getters
    public String getEnquiryID() {
        return enquiryID;
    }

    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    public String getProjectID() {
        return projectID;
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

    public String getResponder() {
        return respondedBy;
    }

    // Setters
    public void setEnquiryID(String enquiryID) {
        this.enquiryID = enquiryID;
    }

    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
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

    public void setResponder(String responder) {
        this.respondedBy = responder;
    }

    // Helpers
    public void markAsResponded(String responder, String response) {
        this.response = response;
        this.enquiryStatus = EnquiryStatus.RESPONDED;
        this.respondedBy = responder;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean isResponse() {
        return response != null && !response.trim().isEmpty();
    }
}
