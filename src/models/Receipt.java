package models;

import java.time.LocalDateTime;

import models.enums.FlatType;
import models.enums.MaritalStatus;

public class Receipt {
    private static int lastReceiptID = 0;

    private String receiptID;
    private String applicantName;
    private String applicantNRIC;
    private int applicantAge;
    private MaritalStatus maritalStatus;
    private FlatType flatType;
    private int flatPrice;
    private String flatUnitNumber;
    private String projectID;
    private String projectName;
    private String projectLocation;
    private LocalDateTime receiptDate;

    public Receipt(Applicant applicant, FlatType flatType, int flatPrice, String flatUnitNumber, Project project) {
        this.receiptID = "RCPT" + (++lastReceiptID);

        // Applicant Info
        this.applicantName = applicant.getName();
        this.applicantNRIC = applicant.getUserNRIC();
        this.applicantAge = applicant.getAge();
        this.maritalStatus = applicant.getMaritalStatus();

        // Flat Info
        this.flatType = flatType;
        this.flatPrice = flatPrice;
        this.flatUnitNumber = flatUnitNumber;

        // Project Info
        this.projectID = project.getProjectID();
        this.projectName = project.getProjectName();
        this.projectLocation = project.getLocation();

        // Timestamp
        this.receiptDate = LocalDateTime.now();
    }

    // Getters
    public String getReceiptID() {
        return receiptID;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantNRIC() {
        return applicantNRIC;
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

    public int getFlatPrice() {
        return flatPrice;
    }

    public String getFlatUnitNumber() {
        return flatUnitNumber;
    }

    public String getProjectID() {
        return projectID;
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