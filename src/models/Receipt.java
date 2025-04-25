package models;

import java.time.LocalDateTime;

import models.enums.FlatType;
import models.enums.MaritalStatus;
import utils.DateTimeUtils;

/**
 * Represents a receipt issued for a housing project booking by an applicant.
 *
 * <p>This class handles receipt details:</p>
 * <ul>
 *     <li>Applicant details</li>
 *     <li>Flat details</li>
 *     <li>Project details</li>
 *     <li>Officer that processed the application</li>
 *     <li>Unique receipt ID and booking timestamp</li>
 * </ul>
 */
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

    /**
     * Constructs a new receipt with the provided details.
     * The receipt ID is automatically generated upon creation.
     *
     * @param applicantName the name of the applicant
     * @param applicantNRIC the NRIC of the applicant
     * @param applicantAge the age of the applicant
     * @param applicantMaritalStatus the marital status of the applicant
     * @param flatType the type of the flat being booked
     * @param flatPrice the price of the flat
     * @param unitNumber the unit number of the booked flat
     * @param projectName the name of the project
     * @param projectID the ID of the project
     * @param projectLocation the location of the project
     * @param officer the officer processing the application
     */
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

    /**
     * Constructs a new receipt for loading from a CSV file, with an existing receipt ID.
     *
     * @param receiptId the receipt ID
     * @param applicantName the name of the applicant
     * @param applicantNRIC the NRIC of the applicant
     * @param applicantAge the age of the applicant
     * @param applicantMaritalStatus the marital status of the applicant
     * @param flatType the type of the flat being booked
     * @param flatPrice the price of the flat
     * @param unitNumber the unit number of the booked flat
     * @param projectName the name of the project
     * @param projectID the ID of the project
     * @param projectLocation the location of the project
     * @param bookingTimestamp the timestamp when the booking was made
     * @param officer the officer who processed the application
     */
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

    /**
     * Gets the receipt ID.
     *
     * @return the receipt ID
     */
    public String getReceiptId() { return receiptId; }

    /**
     * Gets the name of the applicant.
     *
     * @return the applicant's name
     */
    public String getApplicantName() { return applicantName; }

    /**
     * Gets the NRIC of the applicant.
     *
     * @return the applicant's NRIC
     */
    public String getApplicantNRIC() { return applicantNRIC; }

    /**
     * Gets the age of the applicant.
     *
     * @return the applicant's age
     */
    public int getApplicantAge() { return applicantAge; }

    /**
     * Gets the marital status of the applicant.
     *
     * @return the marital status of the applicant
     */
    public MaritalStatus getMaritalStatus() { return maritalStatus; }

    /**
     * Gets the type of the flat being booked.
     *
     * @return the flat type
     */
    public FlatType getFlatType() { return flatType; }

    /**
     * Gets the price of the flat.
     *
     * @return the flat price
     */
    public int getFlatPrice() { return flatPrice; }

    /**
     * Gets the unit number of the booked flat.
     *
     * @return the unit number
     */
    public String getUnitNumber() { return unitNumber; }

    /**
     * Gets the name of the project.
     *
     * @return the project name
     */
    public String getProjectName() { return projectName; }

    /**
     * Gets the project ID.
     *
     * @return the project ID
     */
    public String getProjectID() { return projectID; }

    /**
     * Gets the location of the project.
     *
     * @return the project location
     */
    public String getProjectLocation() { return projectLocation; }

    /**
     * Gets the timestamp when the booking was made.
     *
     * @return the booking timestamp
     */
    public LocalDateTime getBookingTimestamp() { return bookingTimestamp; }

    /**
     * Gets the officer who processed the booking.
     *
     * @return the officer
     */
    public Officer getOfficer() { return officer; }

    /**
     * Generates a unique receipt ID.
     *
     * @return a unique receipt ID
     */
    private String generateReceiptId() {
        return "RCPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
