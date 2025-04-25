package views;

import java.util.*;

/**
 * View class for displaying and managing project filter options.
 */
public class FilterView {

    private static final List<String> LOCATION_OPTIONS = List.of("Jurong West", "Tampines", "Woodlands", "Yishun", "Punggol");
    private static final List<String> FLAT_TYPE_OPTIONS = List.of("2-Room Flexi", "3-Room", "4-Room", "5-Room", "Executive");

    /**
     * Displays the filter menu and allows the user to select filters for projects.
     *
     * @return a map containing the selected filters by category (e.g., Location, Flat Type)
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
     * Displays a sub-menu for selecting or deactivating a filter category.
     *
     * @param categoryName the name of the filter category (e.g., "Location")
     * @param availableOptions the list of available options for the category
     * @param selectedInCategory the set of selected options for the category
     * @return true if the category has no selected options left, false otherwise
     */
    public boolean showFilterSubMenu(String categoryName, List<String> availableOptions, Set<String> selectedInCategory) {
        while (true) {
            List<String> displayOptions = new ArrayList<>();
            for (String option : availableOptions) {
                if (selectedInCategory.contains(option)) {
                    displayOptions.add(option + " (selected)");
                } else {
                    displayOptions.add(option);
                }
            }
            displayOptions.add("Back");

            int choice = CommonView.displayMenu(categoryName + " Filter", displayOptions);
            if (choice <= 0 || choice > availableOptions.size() + 1) {
                continue;
            }

            if (choice == availableOptions.size() + 1) {
                break;
            }

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
