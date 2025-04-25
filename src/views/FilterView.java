package views;

import models.Filter;

import java.util.*;

/**
 * View class for displaying and managing project filter options.
 */
public class FilterView {

    private static final List<String> LOCATION_OPTIONS = List.of("Jurong West", "Tampines", "Woodlands", "Yishun", "Punggol");
    private static final List<String> FLAT_TYPE_OPTIONS = List.of("TWO_ROOM", "THREE_ROOM");
    private static final List<String> PRICE_RANGE_OPTIONS = List.of("300k-400k", "400k-500k", "500k-600k", "600k-700k", "700k-800k", "800k-900k");

    public static List<Filter> selectProjectFilters(Map<String, Set<String>> activeFilters) {
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
                "Filter By Price Range",
                "Apply Filters"
        );
        while (running) {
            int choice = CommonView.displayMenu("Main Filter Menu", options);
            try {
                switch (choice) {
                    case 1:
                        Set<String> selectedLocations = activeFilters.computeIfAbsent("Location", k -> new HashSet<>());
                        showFilterSubMenu("Location", LOCATION_OPTIONS, selectedLocations);
                        if (selectedLocations.isEmpty()) {
                            activeFilters.remove("Location");
                        }
                        break;

                    case 2:
                        Set<String> selectedFlatTypes = activeFilters.computeIfAbsent("Flat Type", k -> new HashSet<>());
                        showFilterSubMenu("Flat Type", FLAT_TYPE_OPTIONS, selectedFlatTypes);
                        if (selectedFlatTypes.isEmpty()) {
                            activeFilters.remove("Flat Type");
                        }
                        break;

                    case 3:
                        Set<String> selectedPriceRange = activeFilters.computeIfAbsent("Price Range", k -> new HashSet<>());
                        showFilterSubMenu("Price Range", PRICE_RANGE_OPTIONS, selectedPriceRange);
                        if (selectedPriceRange.isEmpty()) {
                            activeFilters.remove("Price Range");
                        }
                        break;
                    case 4:
                        System.out.println("\nFilter selection applied.");
                        running = false;
                        break;
                }
            } catch (NumberFormatException e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }

        return formatForOutput(activeFilters);

//        List<Filter> filtersToApply = new ArrayList<>();
//        for (Map.Entry<String, Set<String>> entry : activeFilters.entrySet()) {
//            String key = entry.getKey().toLowerCase().replace(" ", "_");
//            List<String> values = new ArrayList<>(entry.getValue());
//            if (!values.isEmpty()) {
//                filtersToApply.add(new Filter(key, values));
//            }
//        }
    }

    private static void showFilterSubMenu(String categoryName, List<String> availableOptions, Set<String> selectedInCategory) {
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
//        return selectedInCategory.isEmpty();
    }

    private static List<Filter> formatForOutput(Map<String, Set<String>> currentFilters) {
        List<Filter> filtersToApply = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : currentFilters.entrySet()) {
            String key = entry.getKey().toLowerCase().replace(" ", "_");
            List<String> values = new ArrayList<>(entry.getValue());
            if (!values.isEmpty()) {
                filtersToApply.add(new Filter(key, values));
            }
        }
        return filtersToApply;
    }
}
