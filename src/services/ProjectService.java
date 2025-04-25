package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import models.Filter;
import models.Manager;
import models.Project;
import models.Officer;
import models.User;
import models.enums.FlatType;
import models.enums.Role;

import repositories.ProjectRepository;
import repositories.UserRepository;
import repositories.ManagerRepository;

import utils.DateTimeUtils;
import views.CommonView;

/**
 * Service class for managing project-related operations.
 * Provides comprehensive functionality for project management including:
 * <ul>
 *   <li>Project creation, update, and deletion</li>
 *   <li>Project filtering and visibility management</li>
 *   <li>Officer assignment and slot management</li>
 *   <li>Project retrieval by various criteria</li>
 * </ul>
 */
public class ProjectService {

    /**
     * Retrieves projects matching specified filters.
     * Applies all provided filters to the project list. Supported filter keys:
     * <ul>
     *   <li>"location" - Filters by project location</li>
     *   <li>"flat_type" - Filters by available flat types</li>
     * </ul>
     *
     * @param filters List of filters to apply
     * @return List of projects matching all filters
     */
    public static List<Project> getProjects(List<Filter> filters) {
        Stream<Project> projectStream = ProjectRepository.getAll().stream();

        if (!filters.isEmpty()) {
            projectStream = projectStream.filter(project ->
                    filters.stream().allMatch(filter ->
                            checkPassesFilter(project, filter)
                    )
            );
        }

        return projectStream.collect(Collectors.toList());
    }

    /**
     * Checks if a project passes a single filter criteria.
     *
     * @param project The project to check
     * @param filter The filter to apply
     * @return {@code true} if project matches filter, {@code false} otherwise
     */
    private static boolean checkPassesFilter(Project project, Filter filter) {
        if (project == null) {
            return true;
        }

        String key = filter.getKey();
        List<String> value = filter.getValue();
        return switch (key) {
            case "location" -> value.contains(project.getLocation());
            case "flat_type" -> {
                for (FlatType flattype : project.getFlatTypes()) {
                    if (value.contains(flattype.toString())) {
                        yield true;
                    }
                }
                yield false;
            }
            default -> {
                System.err.println("Warning: Unknown filter key skipped: " + key);
                yield true;
            }
        };
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return List of all projects
     */
    public static List<Project> getAllProjects() {
        return ProjectRepository.getAll();
    }

    /**
     * Retrieves projects visible to the current user based on their role.
     * Visibility rules:
     * <ul>
     *   <li>Applicants: Only visible projects within application period</li>
     *   <li>Officers: All projects</li>
     *   <li>Managers: All projects</li>
     * </ul>
     *
     * @return List of visible projects
     */
    public static List<Project> getVisibleProjects() {
        User user = UserRepository.getActiveUser();
        Role userMode;
        if (user.getRole() == Role.OFFICER) {
            userMode = UserRepository.getUserMode();
        } else {
            userMode = user.getRole();
        }

        switch (userMode) {
            case APPLICANT:
                return ProjectRepository.getAll().stream()
                        .filter(Project::isVisible)
                        .filter(p -> p.getApplicationOpenDate().isBefore(DateTimeUtils.getCurrentDateTime())
                                && p.getApplicationCloseDate().isAfter(DateTimeUtils.getCurrentDateTime()))
                        .filter(p -> !p.getOfficers().contains(user.getUserNRIC()))
                        .collect(Collectors.toList());
            case OFFICER:
                return ProjectRepository.getAll().stream()
                        .collect(Collectors.toList());
            default:
                if (user.getRole() == Role.MANAGER) {
                    return ProjectRepository.getAll();
                } else {
                    return List.of();
                }
        }
    }

    /**
     * Retrieves a project by its name.
     *
     * @param projectName Name of the project to find
     * @return The matching project or {@code null} if not found
     */
    public static Project getProjectByName(String projectName) {
        return ProjectRepository.getByName(projectName);
    }

    /**
     * Retrieves the project assigned to a specific officer.
     *
     * @param officer The officer to find project for
     * @return The officer's assigned project or {@code null} if none
     */
    public static Project getProjectByOfficer(Officer officer) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officer.getUserNRIC()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all projects managed by a specific manager.
     *
     * @param manager The manager to find projects for
     * @return List of projects managed by the specified manager
     */
    public static List<Project> getProjectsByManager(Manager manager) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getManagerNRIC().equals(manager.getUserNRIC()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new project with the specified details.
     *
     * @param managerNRIC NRIC of the managing manager
     * @param projectName Name of the project
     * @param location Project location
     * @param startDate Application opening date
     * @param endDate Application closing date
     * @param officerSlots Number of available officer slots
     * @param visible Visibility status
     */
    public static void createProject(String managerNRIC, String projectName, 
            String location, LocalDateTime startDate, LocalDateTime endDate, 
            int officerSlots, boolean visible) {
        Manager manager = ManagerRepository.getByNRIC(managerNRIC);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with NRIC " + managerNRIC + " not found.");
        }

        Project project = new Project(managerNRIC, projectName, location, startDate, endDate, officerSlots, visible);
        
        int availableUnits1 = CommonView.promptInt("Enter number of 2-Room units: ", 0, Integer.MAX_VALUE);
        int availableUnits2 = CommonView.promptInt("Enter number of 3-Room units: ", 0, Integer.MAX_VALUE);

        project.addFlatType(FlatType.TWO_ROOM, availableUnits1, 400000);
        project.addFlatType(FlatType.THREE_ROOM, availableUnits2, 600000);

        System.out.println("PROJECT CREATE 1");
        ProjectRepository.add(project);
        System.out.println("PROJECT CREATE 2");
    }

    /**
     * Updates basic project information.
     *
     * @param project The project to update
     * @param location the new location
     * @param startDate the new application opening date
     * @param endDate the new application closing date
     */
    public static void updateProject(Project project, String location,
                                     LocalDateTime startDate, LocalDateTime endDate) {
        project.setLocation(location);
        project.setApplicationOpenDate(startDate);
        project.setApplicationCloseDate(endDate);
        ProjectRepository.update(project);
    }

    public static void updateProjectDetails(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }
        ProjectRepository.update(project);
    }

    /**
     * Deletes a project by name.
     *
     * @param projectName name of the project to delete
     */
    public static void deleteProject(String projectName) {
        Project project = getProjectByName(projectName);
        if (project != null) {
            ProjectRepository.remove(project);
        } else {
            throw new IllegalArgumentException("Project with name " + projectName + " not found.");
        }
    }

    /**
     * Toggles a project's visibility status.
     *
     * @param projectName the name of the project to update
     * @param visible the new visibility status
     */
    public static void toggleProjectVisibility(String projectName, boolean visible) {
        Project project = getProjectByName(projectName);
        if (project != null) {
            project.setVisible(visible);
            ProjectRepository.saveAll();
        }
    }

    /**
     * Retrieves all projects assigned to a specific officer.
     *
     * @param officerNRIC NRIC of the officer
     * @return List of projects the officer is assigned to
     */
    public static List<Project> getAllOfficersProjects(String officerNRIC) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officerNRIC))
                .collect(Collectors.toList());
    }
}