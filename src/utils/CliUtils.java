package utils;

import java.util.Scanner;

public class CliUtils {
    private static final Scanner scanner = new Scanner(System.in);
    public static int promptInt(String message) {
        while (true) {
            System.out.println(message + " ");
            String inputLine = scanner.nextLine();
            try {
                return Integer.parseInt(inputLine.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    public static boolean promptYesNo(String message) {
        return true;
    }
}
