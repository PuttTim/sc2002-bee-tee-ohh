package views;

import models.Project;
import models.enums.FlatType;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class ProjectView {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void showProjectList(List<Project> projects) {
        CommonView.displayHeader("Available Projects");
        if (projects.isEmpty()) {
            displayEmptyMessage();
            return;
        }
        
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            CommonView.displayMessage(String.format("%d. %s (Location: %s)", 
                i + 1, project.getProjectName(), project.getLocation()));

            List<FlatType> flatTypes = project.getFlatTypes();
            List<Integer> units = project.getFlatTypeUnits();
            List<Integer> prices = project.getFlatTypeSellingPrice();

            StringBuilder flatTypesInfo = new StringBuilder("   Flat Types/Units/Prices: ");
            if (flatTypes.isEmpty()) {
                flatTypesInfo.append("N/A");
            } else {
                for (int j = 0; j < flatTypes.size(); j++) {
                    flatTypesInfo.append(String.format("%s (%d units, $%d)%s",
                        flatTypes.get(j).toString().replace("_", "-"),
                        (j < units.size() ? units.get(j) : 0),
                        (j < prices.size() ? prices.get(j) : 0),
                        j < flatTypes.size() - 1 ? ", " : ""));
                }
            }
            CommonView.displayMessage(flatTypesInfo.toString());
        }
    }

    public static void displayProjectDetails(Project project) {
        CommonView.displayHeader("Project Details");
        CommonView.displayMessage("ID: " + project.getProjectID());
        CommonView.displayMessage("Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        CommonView.displayMessage("Application Period: " + 
            project.getApplicationOpenDate().format(dateTimeFormatter) + " to " +
            project.getApplicationCloseDate().format(dateTimeFormatter));
        
        List<FlatType> flatTypes = project.getFlatTypes();
        List<Integer> units = project.getFlatTypeUnits();
        List<Integer> prices = project.getFlatTypeSellingPrice();
        
        CommonView.displayMessage("Flat Types Available:");
        if (flatTypes.isEmpty()) {
            CommonView.displayMessage("  No flat types defined");
        } else {
            for (int j = 0; j < flatTypes.size(); j++) {
                CommonView.displayMessage(String.format("  - Type: %s, Units: %d, Price: $%d",
                    flatTypes.get(j).toString().replace("_", "-"),
                    (j < units.size() ? units.get(j) : 0),
                    (j < prices.size() ? prices.get(j) : 0)
                ));
            }
        }

        CommonView.displayMessage("Total Officer Slots: " + project.getOfficerSlots());
        CommonView.displayMessage("Assigned Officers: " + project.getOfficers().size() + " " + project.getOfficers());
        CommonView.displayMessage("Applicants: " + project.getApplicants().size() + " " + project.getApplicants());
        CommonView.displaySeparator();
    }

    public static void displayEmptyMessage() {
        CommonView.displayMessage("No projects available.");
    }

    public static int getProjectChoice(int maxChoice) {
        return CommonView.promptInt("\nSelect a project number (or 0 to cancel): ", 0, maxChoice);
    }

    public static int getProjectMenuChoice() {
        List<String> options = List.of(
            "Back",
            "View Project Enquiries",
            "Submit New Enquiry",
            "Apply for Project"
        );
        
        return CommonView.displayMenu("Project Actions", options) - 1;
    }
}
