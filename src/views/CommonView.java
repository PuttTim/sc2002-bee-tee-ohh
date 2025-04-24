package views;

import interfaces.ICommonView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import utils.DateTimeUtils;

/**
 * View class for displaying messages and prompting user input in the console.
 */
public class CommonView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SEPARATOR = "=====================================================================";
    private static final String SEPARATOR_SHORT = "---------------------------------------------------------------------";

    /** Displays a formatted header with the given title. */
    public static void displayHeader(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("       " + title);
        System.out.println(SEPARATOR + "\n");
    }

    /** Displays a plain message. */
    public static void displayMessage(String message) {
        System.out.println(message);
    }

    /** Displays an error message. */
    public static void displayError(String errorMessage) {
        System.out.println("ERROR: " + errorMessage);
    }

    /** Displays a success message. */
    public static void displaySuccess(String successMessage) {
        System.out.println("SUCCESS: " + successMessage);
    }

    /**
     * Prompts the user with a message and returns the input.
     *
     * @param message the prompt message
     * @return the trimmed input string
     */
    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * Prompts the user for an integer input.
     *
     * @param message the prompt message
     * @return the integer entered by the user
     */
    public static int promptInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(prompt(message));
            } catch (NumberFormatException e) {
                displayError("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Prompts the user for an integer input within a range.
     *
     * @param message the prompt message
     * @param min the minimum valid value
     * @param max the maximum valid value
     * @return the valid integer entered by the user
     */
    public static int promptInt(String message, int min, int max) {
        while (true) {
            int value = promptInt(message);
            if (value >= min && value <= max) {
                return value;
            }
            displayError("Invalid choice. Please enter a number between " + min + " and " + max + ".");
        }
    }

    /**
     * Displays a menu with the given title and options.
     *
     * @param title the menu title
     * @param options the list of options
     * @return the selected option number
     */
    public static int displayMenu(String title, List<String> options) {
        displayHeader(title);

        if (options == null || options.isEmpty()) {
            displayMessage("No options available.");
            return -1;
        }

        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        return promptInt("\nEnter your choice: ", 1, options.size());
    }

    /**
     * Displays a menu with a back option.
     *
     * @param title the menu title
     * @param options the list of options
     * @return the selected option number, 0 for back
     */
    public static int displayMenuWithBacking(String title, List<String> options) {
        displayHeader(title);

        if (options == null || options.isEmpty()) {
            displayMessage("No options available.");
            return -1;
        }

        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        System.out.println("0. Back to previous menu");

        return promptInt("\nEnter your choice: ", 0, options.size());
    }

    /** Displays a long separator line. */
    public static void displaySeparator() {
        System.out.println(SEPARATOR);
    }

    /** Displays a short separator line. */
    public static void displayShortSeparator() {
        System.out.println(SEPARATOR_SHORT);
    }

    /** Pauses execution until the user presses Enter. */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Prompts the user for a Yes/No answer.
     *
     * @param message the question to ask
     * @return true if the user answers Yes, false otherwise
     */
    public static boolean promptYesNo(String message) {
        while (true) {
            String response = prompt(message + " (Y/N): ").toUpperCase();
            if (response.equals("Y")) return true;
            if (response.equals("N")) return false;
            displayError("Please enter 'Y' or 'N'.");
        }
    }

    /**
     * Prompts the user for a Yes/No answer (accepts 1/0 as well).
     *
     * @param message the question to ask
     * @return true if the user answers Yes/1, false otherwise
     */
    public static boolean promptYesNo12(String message) {
        while (true) {
            String response = prompt(message + " (Y/N): ").toUpperCase();
            if (response.equals("Y") || response.equals("1")) return true;
            if (response.equals("N") || response.equals("0")) return false;
            displayError("Please enter 'Y' or 'N'.");
        }
    }

    /**
     * Prompts the user to enter a date in dd/MM/yyyy format.
     *
     * @param message the prompt message
     * @return the parsed LocalDateTime
     */
    public static LocalDateTime promptDate(String message) {
        while (true) {
            String dateStr = prompt(message + " (dd/MM/yyyy): ");
            try {
                return DateTimeUtils.parseDateTime(dateStr);
            } catch (Exception e) {
                displayError("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }
}
