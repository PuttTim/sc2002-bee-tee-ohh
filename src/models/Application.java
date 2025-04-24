package models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import models.enums.ApplicationStatus;
import models.enums.FlatType;

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
    public String getApplicationID() {
        return applicationID;
    }

    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    public String getProjectId() {
        return projectId;
    }

    public FlatType getSelectedFlatType() {
        return selectedFlatType;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public boolean isWithdrawalRequested() {
        return isWithdrawalRequested;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public Map<ApplicationStatus, LocalDateTime> getApplicationStatusHistory() {
        return new HashMap<>(applicationStatusHistory);
    }

    public LocalDateTime getApplicationStatusTimestamp(ApplicationStatus status) {
        return applicationStatusHistory.get(status);
    }

    // Setters
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setSelectedFlatType(FlatType selectedFlatType) {
        this.selectedFlatType = selectedFlatType;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    // Helpers
    public void approve(String userNRIC) {
        if (!canApprove()) {
            throw new IllegalStateException("Application cannot be approved in its current state (Status: " + applicationStatus + ", Withdrawal Requested: " + isWithdrawalRequested + ")");
        }
        this.approvedBy = userNRIC; 
        recordStatusChange(ApplicationStatus.SUCCESSFUL);
    }

    public void reject(String userNRIC) { 
        if (!canReject()) {
            throw new IllegalStateException("Application cannot be rejected in its current state (Status: " + applicationStatus + ")");
        }
        this.approvedBy = userNRIC;
        recordStatusChange(ApplicationStatus.UNSUCCESSFUL);
    }

    public void book() {
        if (!canBook()) {
            throw new IllegalStateException("Application cannot be booked in its current state (Status: " + applicationStatus + ", Withdrawal Requested: " + isWithdrawalRequested + ")");
        }
        recordStatusChange(ApplicationStatus.BOOKED);
    }

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