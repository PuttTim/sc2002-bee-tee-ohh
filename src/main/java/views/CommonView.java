package views;

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

    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\033[34m"; // Color for headers
    public static final String ANSI_CYAN = "\u001B[36m"; // Color for menu options

    /** Displays a formatted header with the given title. */
    public static void displayHeader(String title) {
        System.out.println("\n" + ANSI_BLUE + SEPARATOR + ANSI_RESET);
        System.out.println(ANSI_BLUE + "       " + title + ANSI_RESET);
        System.out.println(ANSI_BLUE + SEPARATOR + ANSI_RESET + "\n");
    }

    /** Displays a plain message. */
    public static void displayMessage(String message) {
        System.out.println(message);
    }

    /** Displays an error message. */
    public static void displayError(String errorMessage) {
        System.out.println(ANSI_RED + "ERROR: " + errorMessage + ANSI_RESET);
    }

    /** Displays a success message. */
    public static void displaySuccess(String successMessage) {
        System.out.println(ANSI_GREEN + "SUCCESS: " + successMessage + ANSI_RESET);
    }

    /**
     * Prompts the user with a message and returns the input.
     *
     * @param message the prompt message
     * @return the trimmed input string
     */
    public static String prompt(String message) {
        System.out.print(ANSI_YELLOW + message + ANSI_RESET); // Prompt in yellow
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
            System.out.println(ANSI_CYAN + (i + 1) + ". " + options.get(i) + ANSI_RESET);
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
            System.out.println(ANSI_CYAN + (i + 1) + ". " + options.get(i) + ANSI_RESET);
        }

        System.out.println(ANSI_CYAN + "0. Back to previous menu" + ANSI_RESET);

        return promptInt("\nEnter your choice: ", 0, options.size());
    }

    /** Displays a long separator line. */
    public static void displaySeparator() {
        System.out.println(ANSI_BLUE + SEPARATOR + ANSI_RESET); // Separator in blue like header
    }

    /** Displays a short separator line. */
    public static void displayShortSeparator() {
        System.out.println(ANSI_BLUE + SEPARATOR_SHORT + ANSI_RESET); // Separator in blue like header
    }

    /** Pauses execution until the user presses Enter. */
    public static void pause() {
        System.out.print(ANSI_YELLOW + "\nPress Enter to continue..." + ANSI_RESET); // Pause prompt in yellow
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
                // Use the specific formatter for dd-MM-yyyyTHH:mm:ss
                return DateTimeUtils.parseDateTime(String.format("%sT%s", dateStr.replace("/", "-"), "23:59:59"), DateTimeUtils.DD_MM_YYYY_T_HH_MM_SS_FORMATTER);
            } catch (Exception e) {
                displayError("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }

    public static boolean promptWordConfirmation(String message, String confirmationWord) {
        while (true) {
            // Prompt remains yellow due to the updated prompt() method
            String input = prompt(message + " (Type '" + confirmationWord + "' to confirm, or '0'/'cancel' to cancel): ");
            if (input.equalsIgnoreCase(confirmationWord)) {
                return true;
            }
            if (input.equals("0") || input.equalsIgnoreCase("cancel")) {
                displayMessage("Action cancelled."); // Keep cancellation message default
                return false;
            }
            // Error message uses displayError() which is now red
            displayError("Incorrect confirmation word. Please type '" + confirmationWord + "' exactly, or '0'/'cancel' to cancel.");
        }
    }
}
