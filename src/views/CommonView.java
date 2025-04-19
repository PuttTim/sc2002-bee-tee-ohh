package views;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import interfaces.ICommonView;
import utils.DateTimeUtils;

public class CommonView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SEPARATOR = "=====================================================================";
    private static final String SEPARATOR_SHORT = "---------------------------------------------------------------------";

    public static void displayHeader(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("       " + title);
        System.out.println(SEPARATOR + "\n");
    }

    public static void displayMessage(String message) {
        System.out.println(message);
    }

    public static void displayError(String errorMessage) {
        System.out.println("ERROR: " + errorMessage);
    }

    public static void displaySuccess(String successMessage) {
        System.out.println("SUCCESS: " + successMessage);
    }

    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static int promptInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(prompt(message));
            } catch (NumberFormatException e) {
                displayError("Invalid input. Please enter a whole number.");
            }
        }
    }

    public static int promptInt(String message, int min, int max) {
        while (true) {
            int value = promptInt(message);
            if (value >= min && value <= max) {
                return value;
            }
            displayError("Invalid choice. Please enter a number between " + min + " and " + max + ".");
        }
    }

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

    public static void displaySeparator() {
        System.out.println(SEPARATOR);
    }

    public static void displayShortSeparator() {
        System.out.println(SEPARATOR_SHORT);
    }

    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static boolean promptYesNo(String message) {
        while (true) {
            String response = prompt(message + " (Y/N): ").toUpperCase();
            if (response.equals("Y")) return true;
            if (response.equals("N")) return false;
            displayError("Please enter 'Y' or 'N'.");
        }
    }

    public static boolean promptYesNo12(String message) {
        while (true) {
            String response = prompt(message + " (Y/N): ").toUpperCase();
            if (response.equals("Y") || response.equals("1")) return true;
            if (response.equals("N") || response.equals("0")) return false;
            displayError("Please enter 'Y' or 'N'.");
        }
    }

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