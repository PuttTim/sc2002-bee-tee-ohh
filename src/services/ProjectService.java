package services;

import models.Filter;
import models.Project;
import models.Officer;
import models.enums.FlatType;
import models.User;
import models.enums.Role;
import repositories.ProjectRepository;
import repositories.UserRepository;
import utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class various project-related services such as:
 * <ul>
 *     <li>Filtering</li>
 *     <li>Visibility management</li>
 *     <li>Creation</li>
 *     <li>Updates</li>
 *     <li>Officer assignment</li>
 * </ul>
 */
public class ProjectService {
    /**
     * Returns a list of projects that match the filters.
     *
     * @param filters the list of filters to apply
     * @return a list of filtered projects
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
     * Returns all projects from the repository.
     *
     * @return a list of all projects
     */
    public static List<Project> getAllProjects() {
        return ProjectRepository.getAll();
    }

    /**
     * Returns a list of visible projects based on the current user's role and mode.
     *
     * @return the list of visible projects
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
                        .filter(Project::isVisible)
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
     * Finds and returns a project by its name.
     *
     * @param projectName the name of the project
     * @return the project with the given name, or <code>null</code> if not found
     */
    public static Project getProjectByName(String projectName) {
        return ProjectRepository.getByName(projectName);
    }

    /**
     * Returns the project that an officer is currently assigned to.
     *
     * @param officer the officer to search by
     * @return the assigned project, or <code>null</code> if none found
     */
    public static Project getProjectByOfficer(Officer officer) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officer.getUserNRIC()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a new project and saves it to the repository.
     *
     * @param projectId the ID of the project
     * @param managerNRIC the manager's NRIC
     * @param projectName the name of the project
     * @param location the location of the project
     * @param startDate the start date for applications
     * @param endDate the end date for applications
     * @param officerSlots the number of officer slots
     * @param visible if the project is visible or not
     */
    public static void createProject(String projectId, String managerNRIC, String projectName,
                                     String location, LocalDateTime startDate, LocalDateTime endDate,
                                     int officerSlots, boolean visible) {
        Project newProject = new Project(projectId, managerNRIC, projectName, location,
                startDate, endDate, officerSlots, visible);
        ProjectRepository.add(newProject);
        ProjectRepository.saveAll();
    }

    /**
     * Updates an existing project's location and application dates.
     *
     * @param project the project to update
     * @param location the new location
     * @param startDate the new application start date
     * @param endDate the new application end date
     */
    public static void updateProject(Project project, String location,
                                     LocalDateTime startDate, LocalDateTime endDate) {
        project.setLocation(location);
        project.setApplicationOpenDate(startDate);
        project.setApplicationCloseDate(endDate);
        ProjectRepository.saveAll();
    }

    /**
     * Deletes a project by name.
     *
     * @param projectName the name of the project to delete
     */
    public static void deleteProject(String projectName) {
        Project project = getProjectByName(projectName);
        if (project != null) {
            ProjectRepository.remove(project);
            ProjectRepository.saveAll();
        }
    }

    /**
     * Toggles the visibility of a project.
     *
     * @param projectName the name of the project
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
     * Checks if a project has available officer slots.
     *
     * @param project the project to check for available officer slots
     * @return <code>true</code> if there are officer slots available
     */
    public static boolean hasOfficerSlots(Project project) {
        return project != null && project.getOfficerSlots() > 0;
    }

    /**
     * Assigns an officer to a project and reduces the available officer slots.
     *
     * @param project the project to assign the officer to
     * @param officerNRIC the officer's NRIC
     */
    public static void addOfficerToProject(Project project, String officerNRIC) {
        if (project != null && !project.getOfficers().contains(officerNRIC)) {
            project.addOfficer(officerNRIC);
            project.reduceOfficerSlot();
            ProjectRepository.saveAll();
        }
    }

    /**
     * Removes an officer from a project.
     *
     * @param project the project to remove the officer from
     * @param officerNRIC the officer's NRIC
     */
    public static void removeOfficerFromProject(Project project, String officerNRIC) {
        if (project != null && project.getOfficers().contains(officerNRIC)) {
            project.removeOfficer(officerNRIC);
            ProjectRepository.saveAll();
        }
    }

    /**
     * Returns all projects that the specified officer is involved in.
     *
     * @param officerNRIC the officer's NRIC
     * @return a list of projects that the officer is involved in
     */
    public static List<Project> getAllOfficersProjects(String officerNRIC) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officerNRIC))
                .collect(Collectors.toList());
    }

}
