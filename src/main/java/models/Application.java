package models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import models.enums.ApplicationStatus;
import models.enums.FlatType;

/**
 * Represents an application made by an applicant for a project.
 * <p>
 * This class handles the details of an application, including:
 * <ul>
 *   <li>Managing application status and history</li>
 *   <li>Handling approval, rejection, booking, and withdrawal of applications</li>
 *   <li>Tracking application status changes with timestamps</li>
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
     * <p>Creates a new application with the given details.</p>
     * @param applicantNRIC NRIC of the applicant.
     * @param projectId ID of the project.
     * @param selectedFlatType The flat type selected by the applicant.
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
     * <p>Creates a new application with all details provided (including status history).</p>
     * @param applicationID Unique application ID.
     * @param applicantNRIC NRIC of the applicant.
     * @param projectId ID of the project.
     * @param selectedFlatType The flat type selected by the applicant.
     * @param applicationStatus Current status of the application.
     * @param isWithdrawalRequested Whether withdrawal has been requested.
     * @param applicationDate Date when the application was submitted.
     * @param approvedBy The person who approved or rejected the application.
     * @param statusHistory A map of status history with timestamps.
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
     * <p>Gets the unique ID of the application.</p>
     * @return The application ID.
     */
    public String getApplicationID() {
        return applicationID;
    }

    /**
     * <p>Gets the NRIC of the applicant.</p>
     * @return The applicant's NRIC.
     */
    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    /**
     * <p>Gets the ID of the project the application is for.</p>
     * @return The project ID.
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * <p>Gets the flat type selected by the applicant.</p>
     * @return The selected flat type.
     */
    public FlatType getSelectedFlatType() {
        return selectedFlatType;
    }

    /**
     * <p>Gets the current status of the application.</p>
     * @return The application status.
     */
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * <p>Checks if a withdrawal has been requested for the application.</p>
     * @return True if withdrawal is requested, false otherwise.
     */
    public boolean isWithdrawalRequested() {
        return isWithdrawalRequested;
    }

    /**
     * <p>Gets the date when the application was submitted.</p>
     * @return The application submission date.
     */
    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    /**
     * <p>Gets the person who approved or rejected the application.</p>
     * @return The name of the person who approved or rejected the application.
     */
    public String getApprovedBy() {
        return approvedBy;
    }

    /**
     * <p>Gets a copy of the application status history.</p>
     * @return A map of status changes and their timestamps.
     */
    public Map<ApplicationStatus, LocalDateTime> getApplicationStatusHistory() {
        return new HashMap<>(applicationStatusHistory);
    }

    /**
     * <p>Gets the timestamp when a specific status was recorded.</p>
     * @param status The application status.
     * @return The timestamp when the status was recorded.
     */
    public LocalDateTime getApplicationStatusTimestamp(ApplicationStatus status) {
        return applicationStatusHistory.get(status);
    }

    // Setters

    /**
     * <p>Sets the application ID.</p>
     * @param applicationID The new application ID.
     */
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    /**
     * <p>Sets the applicant's NRIC.</p>
     * @param applicantNRIC The new NRIC.
     */
    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
    }

    /**
     * <p>Sets the project ID.</p>
     * @param projectId The new project ID.
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * <p>Sets the selected flat type.</p>
     * @param selectedFlatType The new flat type.
     */
    public void setSelectedFlatType(FlatType selectedFlatType) {
        this.selectedFlatType = selectedFlatType;
    }

    /**
     * <p>Sets the application submission date.</p>
     * @param applicationDate The new submission date.
     */
    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    // Helpers

    /**
     * <p>Approves the application.</p>
     * @param manager The name of the manager approving the application.
     * @throws IllegalStateException if the application is not pending.
     */
    public void approve(String userNRIC) {
        if (!canApprove()) {
            throw new IllegalStateException("Application cannot be approved in its current state (Status: " + applicationStatus + ", Withdrawal Requested: " + isWithdrawalRequested + ")");
        }
        this.approvedBy = userNRIC; 
        recordStatusChange(ApplicationStatus.SUCCESSFUL);
    }

    /**
     * <p>Rejects the application.</p>
     * @param manager The name of the manager rejecting the application.
     * @throws IllegalStateException if the application is not pending.
     */
    public void reject(String userNRIC) { 
        if (!canReject()) {
            throw new IllegalStateException("Application cannot be rejected in its current state (Status: " + applicationStatus + ")");
        }
        this.approvedBy = userNRIC;
        recordStatusChange(ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * <p>Books the application.</p>
     * @throws IllegalStateException if the application is not successful.
     */
    public void book() {
        if (!canBook()) {
            throw new IllegalStateException("Application cannot be booked in its current state (Status: " + applicationStatus + ", Withdrawal Requested: " + isWithdrawalRequested + ")");
        }
        recordStatusChange(ApplicationStatus.BOOKED);
    }

    /**
     * <p>Requests withdrawal of the application.</p>
     * @throws IllegalStateException if withdrawal has already been requested or if the application cannot be withdrawn.
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
        recordStatusChange(ApplicationStatus.WITHDRAWAL_REQUESTED);
    }

    public void approveWithdrawal(String managerNRIC) {
        if (!canApproveWithdrawal()) {
            throw new IllegalStateException("Withdrawal cannot be approved in its current state");
        }
        this.approvedBy = managerNRIC;
        this.isWithdrawalRequested = false;
        recordStatusChange(ApplicationStatus.WITHDRAWN);
    }

    public void rejectWithdrawal(String managerNRIC) {
        if (!canRejectWithdrawal()) {
            throw new IllegalStateException("Withdrawal cannot be rejected in its current state");
        }
        this.isWithdrawalRequested = false;
        this.approvedBy = managerNRIC;
        recordStatusChange(this.applicationStatus);
    }


    private void recordStatusChange(ApplicationStatus status) {
        applicationStatusHistory.put(status, LocalDateTime.now());
        this.applicationStatus = status;
    }

    public boolean canBook() {
        return applicationStatus == ApplicationStatus.SUCCESSFUL && !isWithdrawalRequested;
    }

    public boolean canWithdraw() {
        return !isWithdrawalRequested &&
                applicationStatus != ApplicationStatus.WITHDRAWN &&
                applicationStatus != ApplicationStatus.UNSUCCESSFUL;
    }

    public boolean canApprove() {
        return applicationStatus == ApplicationStatus.PENDING && !isWithdrawalRequested;
    }

    public boolean canReject() {
        return applicationStatus == ApplicationStatus.PENDING && !isWithdrawalRequested;
    }

    public boolean canApproveWithdrawal() {
        return isWithdrawalRequested;
    }

    public boolean canRejectWithdrawal() {
        return isWithdrawalRequested;
    }
}