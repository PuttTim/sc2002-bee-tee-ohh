package views;

import models.Receipt;
import utils.DateTimeUtils;

/**
 * View class responsible for displaying receipt-related details and success messages.
 * It provides methods to display booking receipt details and success messages when a receipt is generated.
 */
public class ReceiptView {

    /**
     * Displays the details of a booking receipt, including applicant details, booking details, and officer information.
     *
     * @param receipt The receipt whose details are to be displayed
     */
    public static void displayReceiptDetails(Receipt receipt) {
        if (receipt == null) {
            CommonView.displayError("Receipt not found.");
            return;
        }

        CommonView.displayHeader("Booking Receipt - " + receipt.getReceiptId());
        CommonView.displayMessage("Booking Timestamp: " + DateTimeUtils.formatDateTime(receipt.getBookingTimestamp()));
        CommonView.displaySeparator();
        CommonView.displayMessage("Applicant Details:");
        CommonView.displayMessage("  Name: " + receipt.getApplicantName());
        CommonView.displayMessage("  NRIC: " + receipt.getApplicantNRIC());
        CommonView.displayMessage("  Age: " + receipt.getApplicantAge());
        CommonView.displayMessage("  Marital Status: " + receipt.getMaritalStatus());
        CommonView.displaySeparator();
        CommonView.displayMessage("Booking Details:");
        CommonView.displayMessage("  Project Name: " + receipt.getProjectName());
        CommonView.displayMessage("  Project Location: " + receipt.getProjectLocation());
        CommonView.displayMessage("  Flat Type: " + receipt.getFlatType());
        CommonView.displayMessage("  Unit Number: " + receipt.getUnitNumber());
        CommonView.displaySeparator();
        CommonView.displayMessage("Processed by Officer: " + receipt.getOfficer().getName());
        CommonView.displaySeparator();
    }

    /**
     * Displays a success message when a receipt is successfully generated.
     *
     * @param receiptId The ID of the receipt that was successfully generated
     */
    public static void displayReceiptGeneratedSuccess(String receiptId) {
        CommonView.displaySuccess("Receipt " + receiptId + " generated successfully!");
    }
}
