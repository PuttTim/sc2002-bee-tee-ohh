package models;

import java.time.LocalDateTime;

import models.enums.EnquiryStatus;

/**
 * Represents an enquiry made by an applicant about a project.
 * <p>Tracks:</p>
 * <ul>
 *     <li>Status</li>
 *     <li>Query</li>
 *     <li>Response</li>
 *     <li>Manager who responded</li>
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
     * Constructs an enquiry with the given details.
     *
     * @param applicantNRIC the applicant's NRIC.
     * @param projectID the project ID the enquiry is about.
     * @param query the question asked by the applicant.
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
     * Constructs an enquiry with the enquiry ID, project ID, applicant NRIC, and query.
     *
     * @param enquiryID the enquiry ID.
     * @param projectID the project ID the enquiry is about.
     * @param applicantNRIC the applicant's NRIC.
     * @param query the question asked by the applicant.
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
     * Constructs an enquiry with all the details.
     *
     * @param enquiryID the enquiry ID.
     * @param applicantNRIC the applicant's NRIC.
     * @param projectID the project ID.
     * @param query the question asked by the applicant.
     * @param response the response to the enquiry.
     * @param enquiryStatus the status of the enquiry.
     * @param enquiryDate the date when the enquiry was made.
     * @param lastUpdated the last updated timestamp.
     * @param respondedBy the person who responded to the enquiry.
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
     * @return the enquiry ID.
     */
    public String getEnquiryID() {
        return enquiryID;
    }

    /**
     * Gets NRIC of the applicant who made the enquiry.
     *
     * @return the applicant's NRIC.
     */
    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    /**
     * Gets the project ID related to the enquiry.
     *
     * @return the project ID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the question asked by the applicant.
     *
         * @return the question.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Gets the response to the enquiry.
     *
     * @return the response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * Gets the current status of the enquiry.
     *
     * @return the enquiry status.
     */
    public EnquiryStatus getEnquiryStatus() {
        return enquiryStatus;
    }

    /**
     * Gets the date when the enquiry was made.
     *
     * @return the enquiry date.
     */
    public LocalDateTime getEnquiryDate() {
        return enquiryDate;
    }

    /**
     * Gets the timestamp of when the enquiry was last updated.
     *
     * @return the last updated timestamp.
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Gets the name of the person who responded to the enquiry.
     *
     * @return the name of the person who responded.
     */
    public String getRespondedBy() {
        return respondedBy;
    }

    /**
     * Gets the name of the person who responded to the enquiry.
     *
     * @return the name of the responder
     */
    public String getResponder() {
        return respondedBy;
    }

    // Setters
    /**
     * Sets enquiry ID.
     *
     * @param enquiryID the enquiry ID.
     */
    public void setEnquiryID(String enquiryID) {
        this.enquiryID = enquiryID;
    }

    /**
     * Sets applicant's NRIC.
     *
     * @param applicantNRIC the applicant's NRIC.
     */
    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
    }

    /**
     * Sets project ID related to the enquiry.
     *
     * @param projectID the project ID.
     */
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    /**
     * Sets question asked by the applicant.
     *
     * @param query the question asked.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Sets response to the enquiry.
     *
     * @param response the response.
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Sets date when the enquiry was made.
     *
     * @param enquiryDate the enquiry date.
     */
    public void setEnquiryDate(LocalDateTime enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    /**
     * Sets timestamp of when the enquiry was last updated.
     *
     * @param lastUpdated the last updated timestamp.
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Sets name of the person who responded to the enquiry.
     *
     * @param respondedBy the name of the responder.
     */
    public void setRespondedBy(String respondedBy) {
        this.respondedBy = respondedBy;
    }

    /**
     * Sets name of the person who responded to the enquiry.
     *
     * @param responder the name of the responder.
     */
    public void setResponder(String responder) {
        this.respondedBy = responder;
    }

    //Helpers
    /**
     * Marks enquiry as responded, updating the status and adding the response.
     *
     * @param responder the name of the person who responded.
     * @param response the response to the enquiry.
     */
    public void markAsResponded(String responder, String response) {
        this.response = response;
        this.enquiryStatus = EnquiryStatus.RESPONDED;
        this.respondedBy = responder;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Checks if enquiry has a response.
     *
     * @return <code>true</code> if a response is provided, <code>false</code> if no response is provided.
     */
    public boolean isResponse() {
        return response != null && !response.trim().isEmpty();
    }
}
