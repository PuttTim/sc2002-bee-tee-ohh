package views;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import interfaces.ICommonView;
import utils.DateTimeUtils;

/**
 * A view class that handles user interface interactions, including:
 * <ul>
 *     <li>Displaying messages, prompts, menus</li>
 *     <li>Handling user inputs for various types of data</li>
 * </ul>
 */
public class CommonView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SEPARATOR = "=====================================================================";
    private static final String SEPARATOR_SHORT = "---------------------------------------------------------------------";

    /**
     * Displays a header with a title, separated by lines.
     *
     * @param title the title to display in the header
     */
    public static void displayHeader(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("       " + title);
        System.out.println(SEPARATOR + "\n");
    }

    /**
     * Displays a simple message.
     *
     * @param message the message to display
     */
    public static void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message.
     *
     * @param errorMessage the error message to display
     */
    public static void displayError(String errorMessage) {
        System.out.println("ERROR: " + errorMessage);
    }

    /**
     * Displays a success message.
     *
     * @param successMessage the success message to display
     */
    public static void displaySuccess(String successMessage) {
        System.out.println("SUCCESS: " + successMessage);
    }

    /**
     * Prompts the user with a message and returns the user input as a string.
     *
     * @param message the message to display when prompting the user
     * @return the user's input
     */
    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * Prompts the user with a message and returns the user input as an integer.
     *
     * @param message the message to display when prompting the user
     * @return the user's input as an integer
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
     * Prompts the user with a message and ensures the input is within a specified range.
     *
     * @param message the message to display when prompting the user
     * @param min the minimum acceptable value
     * @param max the maximum acceptable value
     * @return the user's input as an integer within the specified range
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
     * Displays a menu with a list of options and prompts the user to choose one.
     *
     * @param title the title to display for the menu
     * @param options the list of options for the user to choose from
     * @return the selected option as an integer
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
     * Displays a menu with options and includes an option to go back to the previous menu.
     *
     * @param title the title to display for the menu
     * @param options the list of options for the user to choose from
     * @return the selected option as an integer
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

        return promptInt("Enter your choice: ", 0, options.size());
    }

    /**
     * Displays a separator line to visually separate sections in the console output.
     */
    public static void displaySeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a shorter separator line to visually separate sections in the console output.
     */
    public static void displayShortSeparator() {
        System.out.println(SEPARATOR_SHORT);
    }

    /**
     * Pauses the program and waits for user to press Enter to continue.
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Prompts the user with a yes/no question and returns the user's response.
     *
     * @param message the question to display
     * @return <code>true</code> if user answers 'Y', <code>false</code> if user answers 'N'
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
     * Prompts the user with a yes/no question with additional support for '1'/'0' as inputs.
     *
     * @param message the question to display
     * @return <code>true</code> if user answers 'Y' or '1',
     * <code>false</code> if user answers 'N' or '0'
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
     * Prompts the user to input a date in the format dd/MM/yyyy and returns the parsed date.
     *
     * @param message the message to display when prompting the user for the date
     * @return the parsed LocalDateTime object
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