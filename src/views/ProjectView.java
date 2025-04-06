package views;

import models.Project;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class ProjectView {
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProjectView() {
        this.scanner = new Scanner(System.in);
    }

    public void showProjectList(List<Project> projects) {
        System.out.println("\nAvailable Projects:");
        System.out.println("------------------");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            System.out.printf("%d. %s (Location: %s)\n", i + 1, project.getProjectName(), project.getNeighbourhood());
            System.out.printf("   Flat Type: %s, Units: %d, Price: $%d\n", 
                project.getFlatType().toString().replace("_", ""), 
                project.getAvailableUnits(), 
                project.getUnitPrice());
            System.out.printf("   Application Period: %s to %s\n",
                project.getApplicationStart().format(dateFormatter),
                project.getApplicationEnd().format(dateFormatter));
            System.out.println();
        }
    }

    public int getProjectChoice(int maxChoice) {
        while (true) {
            System.out.print("Select a project (1-" + maxChoice + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 0) {
                    return 0;
                }
                if (choice >= 1 && choice <= maxChoice) {
                    return choice;
                }
                System.out.println("Please enter a number between 1 and " + maxChoice);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    public int getProjectMenuChoice() {
        System.out.println("\nProject Menu:");
        System.out.println("1. Display all enquiries");
        System.out.println("2. Create new enquiry");
        System.out.println("3. Apply for unit");
        System.out.println("0. Back to previous menu");
        
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 0 && choice <= 3) {
                    return choice;
                }
                System.out.println("Please enter a number between 0 and 3");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }
}
