package models;

import java.time.LocalDateTime;

import models.enums.FlatType;
import models.enums.MaritalStatus;
import utils.DateTimeUtils;

public class Receipt {
    private static int lastReceiptID = 0;

    private String receiptId;
    private String applicantName;
    private String applicantNRIC;
    private int applicantAge;
    private MaritalStatus maritalStatus;
    private FlatType flatType;
    private int flatPrice;
    private String unitNumber;
    private String projectName;
    private String projectID;
    private String projectLocation;
    private LocalDateTime bookingTimestamp;
    private Officer officer; 

    // Constructor
    public Receipt(String applicantName, String applicantNRIC, int applicantAge, MaritalStatus applicantMaritalStatus,
                   FlatType flatType, int flatPrice, String unitNumber, String projectName, String projectID, String projectLocation, 
                   Officer officer) {
        this.receiptId = generateReceiptId();
        this.applicantName = applicantName;
        this.applicantNRIC = applicantNRIC;
        this.applicantAge = applicantAge;
        this.maritalStatus = applicantMaritalStatus;
        this.flatType = flatType;
        this.flatPrice = flatPrice;
        this.unitNumber = unitNumber;
        this.projectName = projectName;
        this.projectID = projectID;
        this.projectLocation = projectLocation;
        this.bookingTimestamp = LocalDateTime.now();
        this.officer = officer;
    }
    
    // Constructor for loading from CSV
    public Receipt(String receiptId, String applicantName, String applicantNRIC, int applicantAge, MaritalStatus applicantMaritalStatus,
                   FlatType flatType, int flatPrice, String unitNumber, String projectName, String projectID, String projectLocation, 
                   LocalDateTime bookingTimestamp, Officer officer) {
        this.receiptId = receiptId;
        this.applicantName = applicantName;
        this.applicantNRIC = applicantNRIC;
        this.applicantAge = applicantAge;
        this.maritalStatus = applicantMaritalStatus;
        this.flatType = flatType;
        this.flatPrice = flatPrice;
        this.unitNumber = unitNumber;
        this.projectName = projectName;
        this.projectID = projectID;
        this.projectLocation = projectLocation;
        this.bookingTimestamp = bookingTimestamp;
        this.officer = officer;
    }


    // Getters
    public String getReceiptId() { return receiptId; }
    public String getApplicantName() { return applicantName; }
    public String getApplicantNRIC() { return applicantNRIC; }
    public int getApplicantAge() { return applicantAge; }
    public MaritalStatus getMaritalStatus() { return maritalStatus; }
    public FlatType getFlatType() { return flatType; }
    public int getFlatPrice() { return flatPrice; }
    public String getUnitNumber() { return unitNumber; }
    public String getProjectName() { return projectName; }
    public String getProjectID() { return projectID; }
    public String getProjectLocation() { return projectLocation; }
    public LocalDateTime getBookingTimestamp() { return bookingTimestamp; }
    public Officer getOfficer() { return officer; }

    private String generateReceiptId() {
        return "RCPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}