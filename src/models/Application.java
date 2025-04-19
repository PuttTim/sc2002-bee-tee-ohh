package models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import models.enums.ApplicationStatus;
import models.enums.FlatType;

/**
 * Represents an application made by an applicant for a project and its flat type.
 * <p>Tracks:</p>
 * <ul>
 *     <li>Status of the application</li>
 *     <li>Application history</li>
 *     <li>Whether application withdrawal has been requested</li>
 * </ul>
 */
public class Application {
    private static int lastApplicationID = 0;

    private String applicationID;
    private String applicantNRIC;
    private String projectId;
    private FlatType selectedFlatType;
    private ApplicationStatus applicationStatus;
    private boolean isWithdrawalRequested;
    private LocalDateTime applicationDate;
    private String approvedBy;
    private Map<ApplicationStatus, LocalDateTime> applicationStatusHistory;

    /**
     * Constructs an Application object with given details.
     *
     * @param applicantNRIC the applicant's NRIC.
     * @param projectId the project ID the application is for.
     * @param selectedFlatType the type of flat for the application.
     */
    public Application(String applicantNRIC, String projectId, FlatType selectedFlatType) {
        this.applicationID = "A" + (++Application.lastApplicationID);
        this.applicantNRIC = applicantNRIC;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.isWithdrawalRequested = false;
        this.applicationDate = LocalDateTime.now();
        this.approvedBy = null;
        this.applicationStatusHistory = new HashMap<>();
        recordStatusChange(ApplicationStatus.PENDING);
    }

    /**
     * Constructs an Application object with all its details.
     *
     * @param applicationID the application ID.
     * @param applicantNRIC the applicant's NRIC.
     * @param projectId the project ID the application is for.
     * @param selectedFlatType the type of flat for the application.
     * @param applicationStatus the status of the application.
     * @param isWithdrawalRequested whether withdrawal has been requested.
     * @param applicationDate the date of application.
     * @param approvedBy the manager who approved the application.
     * @param statusHistory the history of status changes for the application.
     */
    public Application(String applicationID, String applicantNRIC, String projectId,
                      FlatType selectedFlatType, ApplicationStatus applicationStatus,
                      boolean isWithdrawalRequested, LocalDateTime applicationDate,
                      String approvedBy, Map<ApplicationStatus, LocalDateTime> statusHistory) {
        this.applicationID = applicationID;
        this.applicantNRIC = applicantNRIC;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = applicationStatus;
        this.isWithdrawalRequested = isWithdrawalRequested;
        this.applicationDate = applicationDate;
        this.approvedBy = approvedBy;
        this.applicationStatusHistory = statusHistory != null ? new HashMap<>(statusHistory) : new HashMap<>();

        try {
            int numericId = Integer.parseInt(applicationID.replaceAll("\\D+", ""));
            if (numericId > Application.lastApplicationID) {
                Application.lastApplicationID = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // Getters
    /**
     * Gets the application ID.
     *
     * @return the application ID.
     */
    public String getApplicationID() {
        return applicationID;
    }

    /**
     * Gets the NRIC of the applicant.
     *
     * @return the applicant's NRIC.
     */
    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    /**
     * Gets the project ID the application is for.
     *
     * @return the project ID.
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Gets the selected flat type for the application.
     *
     * @return the selected flat type.
     */
    public FlatType getSelectedFlatType() {
        return selectedFlatType;
    }

    /**
     * Gets current status of the application.
     *
     * @return the application status.
     */
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * Checks if withdrawal has been requested for this application.
     *
     * @return <code>true</code> if a withdrawal is requested, <code>false</code> if not.
     */
    public boolean isWithdrawalRequested() {
        return isWithdrawalRequested;
    }

    /**
     * Gets application date.
     *
     * @return the application date.
     */
    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    /**
     * Gets the name of the manager who approved the application.
     *
     * @return the name of the manager, <code>null</code> if application has not been approved.
     */
    public String getApprovedBy() {
        return approvedBy;
    }

    /**
     * Gets history of application status changes.
     *
     * @return a map of application status and status change timestamps.
     */
    public Map<ApplicationStatus, LocalDateTime> getApplicationStatusHistory() {
        return new HashMap<>(applicationStatusHistory);
    }

    /**
     * Gets the timestamp for when a specific application status was recorded.
     *
     * @param status the application status to get timestamp for.
     * @return the timestamp of when the status was recorded.
     */
    public LocalDateTime getApplicationStatusTimestamp(ApplicationStatus status) {
        return applicationStatusHistory.get(status);
    }

    // Setters
    /**
     * Sets application ID.
     *
     * @param applicationID the application ID.
     */
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
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
     * Sets project ID.
     *
     * @param projectId the project ID.
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * Sets selected flat type for the application.
     *
     * @param selectedFlatType the selected flat type.
     */
    public void setSelectedFlatType(FlatType selectedFlatType) {
        this.selectedFlatType = selectedFlatType;
    }

    /**
     * Sets application date.
     *
     * @param applicationDate the application date.
     */
    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    // Helpers
    /**
     * Approves the application and changes application status to SUCCESSFUL.
     *
     * @param manager the manager who approves the application.
     * @throws IllegalStateException if the application is not PENDING, or if withdrawal has been requested.
     */
    public void approve(String manager) {
        if (this.applicationStatus != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only approve pending applications");
        }
        if (this.isWithdrawalRequested) {
            throw new IllegalStateException("Cannot approve an application with withdrawal requested");
        }
        this.applicationStatus = ApplicationStatus.SUCCESSFUL;
        this.approvedBy = manager;
        recordStatusChange(ApplicationStatus.SUCCESSFUL);
    }

    /**
     * Rejects the application and changes application status to UNSUCCESSFUL.
     *
     * @param manager the manager who rejects the application.
     * @throws IllegalStateException if the application is not PENDING.
     */
    public void reject(String manager) {
        if (this.applicationStatus != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending applications");
        }
        this.applicationStatus = ApplicationStatus.UNSUCCESSFUL;
        this.approvedBy = manager;
        recordStatusChange(ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Books the application and changes application status to BOOKED.
     *
     * @throws IllegalStateException if the application is not SUCCESSFUL or if withdrawal has been requested.
     */
    public void book() {
        if (this.applicationStatus != ApplicationStatus.SUCCESSFUL) {
            throw new IllegalStateException("Can only book successful applications");
        }
        if (this.isWithdrawalRequested) {
            throw new IllegalStateException("Cannot book an application with withdrawal requested");
        }
        this.applicationStatus = ApplicationStatus.BOOKED;
        recordStatusChange(ApplicationStatus.BOOKED);
    }

    /**
     * Requests withdrawal of the application and changes application status to WITHDRAWN.
     *
     * @throws IllegalStateException if withdrawal has already been requested or if application is in an invalid state for withdrawal
     */
    public void requestWithdrawal() {
        if (this.isWithdrawalRequested) {
            throw new IllegalStateException("Withdrawal already requested");
        }
        if (this.applicationStatus == ApplicationStatus.WITHDRAWN) {
            throw new IllegalStateException("Application is already withdrawn");
        }
        if (this.applicationStatus == ApplicationStatus.UNSUCCESSFUL) {
            throw new IllegalStateException("Cannot withdraw an unsuccessful application");
        }
        this.isWithdrawalRequested = true;
        this.applicationStatus = ApplicationStatus.WITHDRAWN;
        recordStatusChange(ApplicationStatus.WITHDRAWN);
    }

    private void recordStatusChange(ApplicationStatus status) {
        applicationStatusHistory.put(status, LocalDateTime.now());
    }

    /**
     * Checks if application can be booked.
     *
     * @return <code>true</code> if the application is successful and withdrawal is not requested,
     * <code>false</code> if application is unsuccessful or withdrawal has been requested.
     */
    public boolean canBook() {
        return applicationStatus == ApplicationStatus.SUCCESSFUL && !isWithdrawalRequested;
    }

    /**
     * Checks if application can be withdrawn.
     *
     * @return <code>true</code> if withdrawal is not already requested and application is not in an invalid state for withdrawal.
     */
    public boolean canWithdraw() {
        return !isWithdrawalRequested && 
               applicationStatus != ApplicationStatus.WITHDRAWN && 
               applicationStatus != ApplicationStatus.UNSUCCESSFUL;
    }

    /**
     * Checks if application can be approved.
     *
     * @return <code>true</code> if the application is PENDING and withdrawal is not requested.
     */
    public boolean canApprove() {
        return applicationStatus == ApplicationStatus.PENDING && !isWithdrawalRequested;
    }

    /**
     * Checks if application can be rejected.
     *
     * @return <code>true</code> if the application is PENDING.s
     */
    public boolean canReject() {
        return applicationStatus == ApplicationStatus.PENDING;
    }
}