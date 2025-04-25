package views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import models.Project;
import models.enums.FlatType;

/**
 * View class responsible for handling the display and interaction
 * related to filtering projects.
 */
public class FilterView {

    /**
     * Displays the currently active filters.
     * @param activeFilters A map containing the active filter criteria (e.g., "location", "flatType").
     */
    public static void displayActiveFilters(Map<String, String> activeFilters) {
        if (activeFilters.isEmpty()) {
            CommonView.displayMessage("Filters: None active.");
        } else {
            String filtersStr = activeFilters.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
            CommonView.displayMessage("Active Filters: " + filtersStr);
        }
        CommonView.displayShortSeparator();
    }

    /**
     * Displays the filter menu options.
     * @param activeFilters A map indicating if filters are currently active.
     * @return The user's menu choice.
     */
    public static int displayFilterMenu(Map<String, String> activeFilters) {
        List<String> options = new ArrayList<>();
        options.add("View Project Details");
        options.add("Filter by Location" + (activeFilters.containsKey("location") ? " (Active)" : ""));
        options.add("Filter by Flat Type" + (activeFilters.containsKey("flatType") ? " (Active)" : ""));
        if (!activeFilters.isEmpty()) {
            options.add("Clear All Filters");
        }

        return CommonView.displayMenuWithBacking("Project List Options", options);
    }

    /**
     * Prompts the user to enter a location filter by selecting from available locations.
     * @param projects The list of projects to extract locations from.
     * @return The location string selected by the user, or null to remove the filter.
     */
    public static String promptLocationFilter(List<Project> projects) {
        CommonView.displayMessage("Filter by Location:");
        List<String> locations = projects.stream()
                                         .map(Project::getLocation)
                                         .distinct()
                                         .sorted()
                                         .collect(Collectors.toList());

        if (locations.isEmpty()) {
            CommonView.displayMessage("No specific locations available to filter by in the current project list.");
            return CommonView.prompt("Enter location to filter by (leave blank to remove filter): "); // Fallback to manual input if no locations found
        }

        locations.add(0, "ANY (Remove Filter)"); // Option 1 is now ANY

        int choice = CommonView.displayMenu("Select Location", locations);

        if (choice == 1) { // User chose "ANY"
            return null; // Signal to remove the filter
        } else if (choice > 1 && choice <= locations.size()) {
            return locations.get(choice - 1); // Return selected location
        } else {
            CommonView.displayError("Invalid choice.");
            return promptLocationFilter(projects); // Re-prompt on invalid choice
        }
    }

    /**
     * Prompts the user to select a flat type filter.
     * @return The selected FlatType enum name, or null if the user wants to remove the filter.
     */
    public static String promptFlatTypeFilter() {
        CommonView.displayMessage("Filter by Flat Type:");
        List<String> flatTypeOptions = Stream.of(FlatType.values())
                                             .map(FlatType::getDescription)
                                             .collect(Collectors.toList());
        flatTypeOptions.add(0, "ANY (Remove Filter)"); // Option 1 is now ANY

        int choice = CommonView.displayMenu("Select Flat Type", flatTypeOptions);

        if (choice == 1) { // User chose "ANY"
            return null; // Signal to remove the filter
        } else if (choice > 1 && choice <= flatTypeOptions.size()) {
            String selectedDescription = flatTypeOptions.get(choice - 1);
            // Find the FlatType enum corresponding to the description
            return Stream.of(FlatType.values())
                         .filter(ft -> ft.getDescription().equals(selectedDescription))
                         .findFirst()
                         .map(Enum::name)
                         .orElse(null); 
        } else {
            CommonView.displayError("Invalid choice.");
            return promptFlatTypeFilter();
        }
    }
}
