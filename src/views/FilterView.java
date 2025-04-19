package views;

import controllers.ApplicantController;
import controllers.EnquiryController;
import controllers.ProjectController;
import models.Filter;

import java.util.*;

/**
 * A view class that handles the display of filter options for projects
 * and manages the selection of filters.
 */
public class FilterView {

    // Predefined filter options for location and flat type
    private static final List<String> LOCATION_OPTIONS = List.of("Jurong West", "Tampines", "Woodlands", "Yishun", "Punggol");
    private static final List<String> FLAT_TYPE_OPTIONS = List.of("2-Room Flexi", "3-Room", "4-Room", "5-Room", "Executive");

    /**
     * Displays the main filter menu and allows the user to select and apply filters.
     *
     * @return a map of selected filters where the key is the filter category,
     * and the value is the set of selected filter options
     */
    public Map<String, Set<String>> selectProjectFilters() {
        Map<String, Set<String>> activeFilters = new HashMap<>();
        boolean running = true;
        List<String> options = List.of(
                "Filter By Location",
                "Filter By Flat Type",
                "Apply Filters"
        );

        while (running) {
            int choice = CommonView.displayMenu("Main Filter Menu", options);
            try {
                switch (choice) {
                    case 1:
                        Set<String> selectedLocations = activeFilters.computeIfAbsent("Location", k -> new HashSet<>());
                        if(showFilterSubMenu("Location", LOCATION_OPTIONS, selectedLocations)) {
                            activeFilters.remove("Location");
                        }
                        break;

                    case 2:
                        Set<String> selectedFlatTypes = activeFilters.computeIfAbsent("Flat Type", k -> new HashSet<>());
                        if(showFilterSubMenu("Flat Type", FLAT_TYPE_OPTIONS, selectedFlatTypes)) {
                            activeFilters.remove("Flat Type");
                        }
                        break;

                    case 3:
                        System.out.println("\nFilter selection confirmed.");
                        running = false;
                        break;
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
        return activeFilters;
    }

    /**
     * Displays a submenu for a specific filter category
     * and allows the user to toggle filters on or off.
     *
     * @param categoryName the name of the filter category (e.g., "Location")
     * @param availableOptions a list of available filter options
     * @param selectedInCategory the set of currently selected options for the category
     * @return <code>true</code> if the category has no active filters after this operation,
     * otherwise <code>false</code>
     */
    public boolean showFilterSubMenu(String categoryName, List<String> availableOptions, Set<String> selectedInCategory) {
        while (true) {
            List<String> displayOptions = new ArrayList<>();
            // Display each available option, marking selected ones
            for (String option : availableOptions) {
                if (selectedInCategory.contains(option)) {
                    displayOptions.add(option + " (selected)");
                } else {
                    displayOptions.add(option);
                }
            }
            displayOptions.add("Back");

            int choice = CommonView.displayMenu(categoryName + " Filter", displayOptions);

            // If invalid choice, continue
            if (choice <= 0 || choice > availableOptions.size() + 1) {
                continue;
            }

            // Go back to the previous menu
            if (choice == availableOptions.size() + 1) {
                break;
            }

            // Toggle filter selection
            int chosenIndex = choice - 1;
            String selectedOption = availableOptions.get(chosenIndex);

            if (selectedInCategory.contains(selectedOption)) {
                selectedInCategory.remove(selectedOption);
                System.out.println("'" + selectedOption + "' filter DEACTIVATED.");
            } else {
                selectedInCategory.add(selectedOption);
                System.out.println("'" + selectedOption + "' filter ACTIVATED.");
            }
        }
        return selectedInCategory.isEmpty();
    }
}
