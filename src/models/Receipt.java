package models;

import java.time.LocalDateTime;

import models.enums.FlatType;
import models.enums.MaritalStatus;

/**
 * Represents the receipt generated when an applicant successfully books a flat.
 */
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

    /**
     * Creates a Receipt object using the applicant, flat, and project details.
     *
     * @param applicant the applicant who booked the flat
     * @param flatType the type of flat booked
     * @param flatPrice the selling price of the flat
     * @param flatUnitNumber the unit number of the booked flat
     * @param project the project where the flat is located
     */
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
    /**
     * Gets the receipt ID.
     *
     * @return the receipt ID
     */
    public String getReceiptID() {
        return receiptID;
    }

    /**
     * Gets the applicant's name.
     *
     * @return the name of the applicant
     */
    public String getApplicantName() {
        return applicantName;
    }

    /**
     * Gets the applicant's NRIC.
     *
     * @return the NRIC of the applicant
     */
    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    /**
     * Gets the applicant's age.
     *
     * @return the age of the applicant
     */
    public int getApplicantAge() {
        return applicantAge;
    }

    /**
     * Gets the marital status of the applicant.
     *
     * @return the marital status
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Gets the flat type.
     *
     * @return the type of flat booked
     */
    public FlatType getFlatType() {
        return flatType;
    }

    /**
     * Gets the flat price.
     *
     * @return the selling price of the flat
     */
    public int getFlatPrice() {
        return flatPrice;
    }

    /**
     * Gets the flat unit number.
     *
     * @return the unit number of the flat
     */
    public String getFlatUnitNumber() {
        return flatUnitNumber;
    }

    /**
     * Gets the project ID.
     *
     * @return the ID of the project
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the project name.
     *
     * @return the name of the project
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the project location.
     *
     * @return the location of the project
     */
    public String getProjectLocation() {
        return projectLocation;
    }

    /**
     * Gets the receipt creation date and time.
     *
     * @return the date and time the receipt was generated
     */
    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }
}