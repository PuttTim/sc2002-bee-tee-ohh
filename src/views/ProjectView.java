package views;

import models.Project;
import models.enums.FlatType;

import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

import interfaces.IProjectView;

public class ProjectView implements IProjectView {
    private final Scanner scanner;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ProjectView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void showProjectList(List<Project> projects) {
        System.out.println("\nAvailable Projects:");
        System.out.println("------------------");
        if (projects.isEmpty()) {
            displayEmptyMessage();
            return;
        }
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            System.out.printf("%d. %s (Location: %s)\n", i + 1, project.getProjectName(), project.getLocation());

            List<FlatType> flatTypes = project.getFlatTypes();
            List<Integer> units = project.getFlatTypeUnits();
            List<Integer> prices = project.getFlatTypeSellingPrice();

            System.out.print("   Flat Types/Units/Prices: ");
            if (flatTypes.isEmpty()) {
                System.out.print("N/A");
            } else {
                for (int j = 0; j < flatTypes.size(); j++) {
                    System.out.printf("%s (%d units, $%d)%s",
                        flatTypes.get(j).toString().replace("_", "-"),
                        (j < units.size() ? units.get(j) : 0),
                        (j < prices.size() ? prices.get(j) : 0),
                        (j < flatTypes.size() - 1 ? " | " : "")
                    );
                }
            }
            System.out.println();

            System.out.printf("   Application Period: %s to %s\n",
                project.getApplicationOpenDate().format(dateTimeFormatter),
                project.getApplicationCloseDate().format(dateTimeFormatter));
            System.out.printf("   Officer Slots Available: %d / %d\n",
                 project.getOfficerSlots() - project.getOfficers().size(),
                 project.getOfficerSlots());
            System.out.println();
        }
    }

    @Override
    public void displayEmptyMessage() {
        System.out.println("No projects found.");
    }

    @Override
    public void displayProjectDetails(Project project) {
        System.out.println("\n--- Project Details ---");
        System.out.println("Project ID: " + project.getProjectID());
        System.out.println("Name: " + project.getProjectName());
        System.out.println("Location: " + project.getLocation());
        System.out.println("Manager NRIC: " + project.getManagerNRIC());
        System.out.println("Visible to Applicants: " + (project.isVisible() ? "Yes" : "No"));
        System.out.println("Application Open: " + project.getApplicationOpenDate().format(dateTimeFormatter));
        System.out.println("Application Close: " + project.getApplicationCloseDate().format(dateTimeFormatter));

        List<FlatType> flatTypes = project.getFlatTypes();
        List<Integer> units = project.getFlatTypeUnits();
        List<Integer> prices = project.getFlatTypeSellingPrice();
        System.out.println("Flat Information:");
        if (flatTypes.isEmpty()) {
            System.out.println("  N/A");
        } else {
            for (int j = 0; j < flatTypes.size(); j++) {
                System.out.printf("  - Type: %s, Units: %d, Price: $%d\n",
                    flatTypes.get(j).toString().replace("_", "-"),
                    (j < units.size() ? units.get(j) : 0),
                    (j < prices.size() ? prices.get(j) : 0)
                );
            }
        }

        System.out.println("Total Officer Slots: " + project.getOfficerSlots());
        System.out.println("Assigned Officers: " + project.getOfficers().size() + " " + project.getOfficers());
        System.out.println("Applicants: " + project.getApplicants().size() + " " + project.getApplicants());
        System.out.println("-----------------------");
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
        System.out.println("1. Display all enquiries for this project");
        System.out.println("2. Create new enquiry for this project");
        System.out.println("3. Apply for unit in this project");
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
