package models;

import java.time.LocalDateTime;

import models.enums.EnquiryStatus;

/**
 * Represents an enquiry made by an applicant regarding a project.
 * <p>
 * This class handles the details of an enquiry, including:
 * <ul>
 *   <li>Storing enquiry information such as query and status</li>
 *   <li>Allowing responses to be added to enquiries</li>
 *   <li>Tracking the status and history of the enquiry</li>
 * </ul>
 */
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

    /**
     * Constructor to create a new enquiry.
     *
     * @param applicantNRIC the applicant's NRIC
     * @param projectID the project ID the enquiry is about
     * @param query the applicant's question or query
     */
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

    /**
     * Constructor for creating an enquiry from existing data.
     *
     * @param enquiryID the enquiry ID
     * @param projectID the project ID the enquiry is about
     * @param applicantNRIC the applicant's NRIC
     * @param query the applicant's question or query
     */
    public Enquiry(String enquiryID, String projectID, String applicantNRIC, String query) {
        this.enquiryID = enquiryID;
        this.projectID = projectID;
        this.applicantNRIC = applicantNRIC;
        this.query = query;
        this.response = null;
        this.respondedBy = null;
    }

    /**
     * Constructor for creating an enquiry with all data.
     *
     * @param enquiryID the enquiry ID
     * @param applicantNRIC the applicant's NRIC
     * @param projectID the project ID the enquiry is about
     * @param query the applicant's question or query
     * @param response the response to the enquiry
     * @param enquiryStatus the status of the enquiry
     * @param enquiryDate the date the enquiry was made
     * @param lastUpdated the last time the enquiry was updated
     * @param respondedBy the person who responded to the enquiry
     */
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

    /**
     * Gets the enquiry ID.
     *
     * @return the enquiry ID
     */
    public String getEnquiryID() {
        return enquiryID;
    }

    /**
     * Gets the applicant's NRIC.
     *
     * @return the applicant's NRIC
     */
    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    /**
     * Gets the project ID of the project the enquiry is related to.
     *
     * @return the project ID the enquiry is about
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the contents of the applicant's enquiry.
     *
     * @return the applicant's query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Gets the enquiry's response.
     *
     * @return the response to the enquiry
     */
    public String getResponse() {
        return response;
    }

    /**
     * Gets the current status of the enquiry.
     *
     * @return the status of the enquiry
     */
    public EnquiryStatus getEnquiryStatus() {
        return enquiryStatus;
    }

    /**
     * Gets when the enquiry was made.
     *
     * @return the date the enquiry was made
     */
    public LocalDateTime getEnquiryDate() {
        return enquiryDate;
    }

    /**
     * Gets when the enquiry was last updated.
     *
     * @return the last time the enquiry was updated
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Gets who responded to the enquiry.
     *
     * @return the person who responded to the enquiry
     */
    public String getRespondedBy() {
        return respondedBy;
    }

    /**
     * Gets who responded to the enquiry.
     *
     * @return the person who responded to the enquiry
     */
    public String getResponder() {
        return respondedBy;
    }

    // Setters

    /**
     * Sets the enquiry ID.
     *
     * @param enquiryID the enquiry ID
     */
    public void setEnquiryID(String enquiryID) {
        this.enquiryID = enquiryID;
    }

    /**
     * Sets the applicant's NRIC.
     *
     * @param applicantNRIC the applicant's NRIC
     */
    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
    }

    /**
     * Sets the project ID.
     *
     * @param projectID the project ID
     */
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    /**
     * Sets the applicant's query.
     *
     * @param query the applicant's query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Sets the response to the enquiry.
     *
     * @param response the response to the enquiry
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Sets the date the enquiry was made.
     *
     * @param enquiryDate the enquiry date
     */
    public void setEnquiryDate(LocalDateTime enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    /**
     * Sets the last time the enquiry was updated.
     *
     * @param lastUpdated the last updated time
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Sets the person who responded to the enquiry.
     *
     * @param respondedBy the person who responded
     */
    public void setRespondedBy(String respondedBy) {
        this.respondedBy = respondedBy;
    }

    /**
     * Sets the responder.
     *
     * @param responder the person responding
     */
    public void setResponder(String responder) {
        this.respondedBy = responder;
    }

    // Helpers

    /**
     * Marks the enquiry as responded and stores the response.
     *
     * @param responder the person who responded
     * @param response the response to the enquiry
     */
    public void markAsResponded(String responder, String response) {
        this.response = response;
        this.enquiryStatus = EnquiryStatus.RESPONDED;
        this.respondedBy = responder;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Checks if the enquiry has been responded to.
     *
     * @return true if the enquiry has a response, false otherwise
     */
    public boolean isResponse() {
        return response != null && !response.trim().isEmpty();
    }
}
