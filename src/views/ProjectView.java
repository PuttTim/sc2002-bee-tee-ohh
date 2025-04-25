package views;

import controllers.EnquiryController;
import models.Applicant;
import models.Manager;
import models.Officer;
import models.Project;
import models.Registration;
import models.enums.FlatType;
import repositories.ApplicationRepository;
import repositories.ManagerRepository;
import repositories.ProjectRepository;
import services.ProjectService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * View class responsible for displaying project-related functionalities.
 * It provides methods for displaying project details, creating enquiries,
 * and managing officer registration status for projects.
 */
public class ProjectView {
    private static final EnquiryController enquiryController = new EnquiryController();
    private static final ProjectService projectService = ProjectService.getInstance();

    /**
     * Displays the project menu for an applicant, allowing them to view project details or create an enquiry.
     *
     * @param applicant The applicant interacting with the menu
     * @param projects The list of available projects for the applicant
     */
    public static void showProjectMenu(Applicant applicant, List<Project> projects) {
        List<String> options = List.of(
                "View Project Details",
                "Create New Enquiry"
        );

        while (true) {
            int choice = CommonView.displayMenuWithBacking("Project Menu", options);
            try {
                switch (choice) {
                    case 1 -> {
                        int projectChoice = getProjectChoice(projects);
                        if (projectChoice != -1) {
                            Project selectedProject = projects.get(projectChoice - 1);
                            displayProjectDetails(selectedProject);
                        }
                    }
                    case 2 -> enquiryController.createNewEnquiry(applicant);
                    case 0 -> {
                        return;
                    }
                }
            } catch (Exception e) {
                CommonView.displayError("Please enter a valid number!");
            }
        }
    }

    /**
     * Displays a list of available projects to the user, including basic details.
     *
     * @param projects The list of available projects to be displayed
     */
    public static void displayProjectList(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);

            CommonView.displayMessage(String.format("%d. %s", i + 1, project.getProjectName()));
            CommonView.displayMessage(String.format("   Location: %s", project.getLocation()));
            CommonView.displayMessage(String.format("   Application Period: %s to %s",
                project.getApplicationOpenDate(), project.getApplicationCloseDate()));

            List<FlatType> flatTypes = project.getFlatTypes();
            List<Integer> units = project.getFlatTypeUnits();
            StringBuilder flatTypesBuilder = new StringBuilder();
            for (int j = 0; j < flatTypes.size(); j++) {
                flatTypesBuilder.append(flatTypes.get(j).getDescription())
                                .append(" (").append(units.get(j)).append(" units)");
                if (j < flatTypes.size() - 1) {
                    flatTypesBuilder.append(", ");
                }
            }
            String flatTypesString = flatTypesBuilder.length() > 0 ? flatTypesBuilder.toString() : "N/A";
            CommonView.displayMessage(String.format("   Available Flat Types: %s", flatTypesString));
        }
    }

    /**
     * Displays a list of available projects with detailed information.
     *
     * @param projects The list of available projects to be displayed
     */
    public static void displayAvailableProjects(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available matching the current filters.");
            return;
        }

        // Display list first
        displayProjectList(projects);
        CommonView.displayShortSeparator();
    }

    /**
     * Displays a list of projects and provides filtering options.
     * Allows users to view details, filter by location/flat type, or clear filters.
     *
     * @param initialProjects The initial list of projects to display and filter.
     * @param title The title to display for the view.
     */
    public static void displayAndFilterProjects(List<Project> initialProjects, String title) {
        Map<String, String> activeFilters = new HashMap<>();
        List<Project> currentProjects = initialProjects;
        boolean running = true;

        while (running) {
            CommonView.displayHeader(title);
            FilterView.displayActiveFilters(activeFilters);
            displayAvailableProjects(currentProjects);

            int choice = FilterView.displayFilterMenu(activeFilters);

            try {
                switch (choice) {
                    case 1: // View Project Details
                        if (currentProjects.isEmpty()) {
                            CommonView.displayError("No projects to view details for.");
                            CommonView.prompt("Press Enter to continue...");
                            break;
                        }
                        int projectChoice = getProjectChoice(currentProjects);
                        if (projectChoice != -1) {
                            Project selectedProject = currentProjects.get(projectChoice - 1);
                            displayProjectDetails(selectedProject);
                            CommonView.prompt("Press Enter to continue...");
                        }
                        break;
                    case 2: // Filter by Location
                        String location = FilterView.promptLocationFilter(currentProjects);
                        if (location == null) { // User chose 'ANY' or left blank in fallback
                            activeFilters.remove("location");
                        } else {
                            activeFilters.put("location", location.trim());
                        }
                        currentProjects = projectService.getFilteredProjects(activeFilters);
                        break;
                    case 3: // Filter by Flat Type
                        String flatType = FilterView.promptFlatTypeFilter();
                        if (flatType == null) {
                            activeFilters.remove("flatType");
                        } else {
                            activeFilters.put("flatType", flatType);
                        }
                        currentProjects = projectService.getFilteredProjects(activeFilters);
                        break;
                    case 4: // This case is only valid if 'Clear All Filters' was displayed
                        if (!activeFilters.isEmpty()) { // Check if the option was actually available
                            activeFilters.clear();
                            currentProjects = projectService.getFilteredProjects(activeFilters);
                            CommonView.displaySuccess("All filters cleared.");
                        } else {
                            CommonView.displayError("Invalid choice!");
                        }
                        break;
                    case 0: // Back
                        running = false;
                        break;
                    default:
                        CommonView.displayError("Invalid choice!");
                        break;
                }
            } catch (Exception e) {
                CommonView.displayError("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays detailed information about a specific project.
     *
     * @param project The project whose details are to be displayed
     */
    public static void displayProjectDetails(Project project) {
        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }

    /**
     * Displays detailed information about a project from an officer's perspective.
     *
     * @param project The project whose details are to be displayed
     */
    public static void displayProjectDetailsOfficerView(Project project) {
        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        Manager manager = ManagerRepository.getByNRIC(project.getManagerNRIC());
        CommonView.displayMessage("Manager: " + (manager != null ? String.format("%s (%s)", manager.getName(), manager.getUserNRIC()) : "N/A"));
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }


    /**
     * Displays detailed information about a specific project in the Manager view.
     *
     * This method prints the project name, location, manager details, application period,
     * available flat types, and their corresponding unit counts and prices.
     *
     * @param project The project which details are to be displayed.
     */
    public static void displayProjectDetailsManagerView(Project project) {
        CommonView.displayShortSeparator();

        CommonView.displayMessage("Project Name: " + project.getProjectName());
        CommonView.displayMessage("Location: " + project.getLocation());
        Manager manager = ManagerRepository.getByNRIC(project.getManagerNRIC());
        CommonView.displayMessage("Manager: " + (manager != null ? String.format("%s (%s)", manager.getName(), manager.getUserNRIC()) : "N/A"));
        CommonView.displayMessage("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
        CommonView.displayMessage("Available Flat Types:");
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + flatTypes.get(i).getDescription());
            CommonView.displayMessage("   Available Units: " + project.getFlatTypeUnits().get(i));
            CommonView.displayMessage("   Price: " + project.getFlatTypeSellingPrice().get(i));
        }
    }


    /**
     * Displays officer registration status for projects handled by the officer.
     *
     * @param projects The list of projects to be displayed
     * @param officerRegistrations The list of officer registrations to be checked
     * @param officer The officer whose registration details are being displayed
     */
    public static void displayOfficerRegistrations(List<Project> projects, List<Registration> officerRegistrations, Officer officer) {
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        CommonView.displayHeader("Projects and their Officer Registrations");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            CommonView.displayMessage((i + 1) + ". Project: " + project.getProjectName());

            if (officerRegistrations.stream().anyMatch(r -> r.getProjectID().equals(project.getProjectID()))) {
                officerRegistrations.stream()
                        .filter(r -> r.getProjectID().equals(project.getProjectID()) && r.getOfficer().getUserNRIC().equals(officer.getUserNRIC()))
                        .forEach(r -> CommonView.displayMessage(
                                "   Status: " + r.getRegistrationStatus() + " AS Officer" +
                                        "\n      Registration Date: " + r.getRegistrationDate() +
                                        "\n      Last Updated: " + r.getLastUpdated() +
                                        "\n      Approved By: " + (r.getApprovedBy() != null ? r.getApprovedBy().getName() : "N/A"
                                )));
            }
            else if (ApplicationRepository.getByProject(project.getProjectID()).stream().anyMatch(a -> a.getApplicantNRIC().equals(officer.getUserNRIC()))) {
                CommonView.displayMessage("   Status: Registered as Applicant");
            }
            else {
                CommonView.displayMessage("   Status: Not Registered");
            }
            CommonView.displayMessage("   Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());
            List<String> officersNames = ProjectRepository.getProjectOfficerNames(project.getProjectID());
            CommonView.displayMessage("   Available Officer Slots: " + project.getOfficerSlots());

            if (officersNames.isEmpty()) {
                CommonView.displayMessage("   No officers registered");
            } else {
                CommonView.displayMessage("   Registered Officers:");
                for (String name : officersNames) {
                    CommonView.displayMessage("     - " + name);
                }
            }
            CommonView.displayMessage("----------------------------------------");
        }
    }

    /**
     * Prompts the user to enter a project name.
     *
     * @return The project name entered by the user
     */
    public static String getProjectName() {
        return CommonView.prompt("Enter project name: ");
    }

    /**
     * Prompts the user to enter a project location.
     *
     * @return The project location entered by the user
     */
    public static String getProjectLocation() {
        return CommonView.prompt("Enter project location: ");
    }

    /**
     * Prompts the user to enter a project ID.
     *
     * @return The project ID entered by the user
     */
    public static String getProjectId() {
        return CommonView.prompt("Enter project ID: ");
    }

    /**
     * Prompts the user to enter the number of officer slots available for a project.
     *
     * @return The number of officer slots entered by the user
     */
    public static int getOfficerSlots() {
        return CommonView.promptInt("Enter the number of Officer slots: ");
    }

    /**
     * Prompts the user to determine the visibility status of a project.
     *
     * @return A boolean indicating whether the project is visible or not
     */
    public static boolean getProjectVisibility() {
        return CommonView.promptYesNo("Make project visible?: ");
    }

    /**
     * Prompts the user to select a project from a list.
     *
     * @param projects The list of projects to choose from
     * @return The index of the selected project
     */
    public static int getProjectChoice(List<Project> projects) {
        if (projects.isEmpty()) {
            CommonView.displayError("No projects available.");
            return -1;
        }

        CommonView.displayHeader("Select a project to view details:");
        for (int i = 0; i < projects.size(); i++) {
            CommonView.displayMessage((i + 1) + ". " + projects.get(i).getProjectName());
        }
        return CommonView.promptInt("\nEnter your choice: ", 1, projects.size());
    }

    /**
     * Displays a success message after a project has been created.
     *
     * @param projectName The name of the project created
     */
    public static void displayProjectCreationSuccess(String projectName) {
        CommonView.displaySuccess("Project \"" + projectName + "\" created successfully.");
    }

    /**
     * Displays a success message after a project has been updated.
     */
    public static void displayProjectUpdateSuccess() {
        CommonView.displaySuccess("Project updated successfully.");
    }

    /**
     * Displays a success message after a project has been deleted.
     */
    public static void displayProjectDeleteSuccess() {
        CommonView.displaySuccess("Project deleted successfully.");
    }

    /**
     * Displays an error message when a project is not found.
     */
    public static void displayProjectNotFound() {
        CommonView.displayError("Project not found!");
    }

    /**
     * Displays a success message after the visibility of a project has been updated.
     */
    public static void displayVisibilityUpdateSuccess() {
        CommonView.displaySuccess("Project visibility updated successfully.");
    }
}
