package models;

import java.time.LocalDateTime;

import enums.FlatType;
import enums.MaritalStatus;

public class Receipt {
    private static int lastReceiptId = 0;

    private String receiptId;
    private String applicantName;
    private String applicantNric;
    private int applicantAge;
    private MaritalStatus maritalStatus;
    private FlatType flatType;
    private String projectId;
    private String projectName;
    private String projectLocation;
    private LocalDateTime receiptDate;

    public Receipt(Applicant applicant, FlatType flatType, Project project) {
        this.receiptId = "RCPT" + (++lastReceiptId);

        // Applicant Info
        this.applicantName = applicant.getName();
        this.applicantNric = applicant.getUserNric();
        this.applicantAge = applicant.getAge();
        this.maritalStatus = applicant.getMaritalStatus();

        // Flat Info
        this.flatType = flatType;

        // Project Info
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.projectLocation = project.getLocation();

        // Timestamp
        this.receiptDate = LocalDateTime.now();
    }

    // Getters
    public String getReceiptId() {
        return receiptId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantNric() {
        return applicantNric;
    }

    public int getApplicantAge() {
        return applicantAge;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }
}