package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import interfaces.IProjectService;
import models.Manager;
import models.Project;
import models.Officer;
import models.User;
import models.enums.FlatType;
import models.enums.MaritalStatus;
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
public class ProjectService implements IProjectService {
    private static ProjectService instance;

    private ProjectService() {}
    
    public static ProjectService getInstance() {
        if (instance == null) {
            instance = new ProjectService();
        }
        return instance;
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return List of all projects
     */
    @Override
    public List<Project> getAllProjects() {
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
    @Override
    public List<Project> getVisibleProjects() {
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
                        .filter(p -> {
                            List<FlatType> flatTypes = p.getFlatTypes();

                            if (user.getMaritalStatus() == MaritalStatus.MARRIED && user.getAge() >= 21) {
                                return true;
                            } else if ((user.getMaritalStatus() == MaritalStatus.SINGLE || user.getMaritalStatus() == MaritalStatus.DIVORCED) && user.getAge() >= 35) {
                                return flatTypes.contains(FlatType.TWO_ROOM);
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
            case OFFICER:
                return new ArrayList<>(ProjectRepository.getAll());
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
    @Override
    public Project getProjectByName(String projectName) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getProjectName().equals(projectName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the project assigned to a specific officer.
     *
     * @param officer The officer to find projects for
     * @return The project assigned to the officer, or null if not found
     */
    @Override
    public Project getProjectByOfficer(Officer officer) {
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
    @Override
    public List<Project> getProjectsByManager(Manager manager) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getManagerNRIC().equals(manager.getUserNRIC()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of projects filtered by the given criteria.
     * Only visible projects are considered.
     *
     * @param filters A map containing filter criteria (e.g., "location", "flatType").
     * @return A list of filtered, visible projects.
     */
    public List<Project> getFilteredProjects(Map<String, String> filters) {
        Stream<Project> projectStream = getVisibleProjects().stream();

        String locationFilter = filters.get("location");
        if (locationFilter != null && !locationFilter.isEmpty()) {
            projectStream = projectStream.filter(p -> p.getLocation().equalsIgnoreCase(locationFilter));
        }

        String flatTypeFilter = filters.get("flatType");
        if (flatTypeFilter != null && !flatTypeFilter.isEmpty()) {
            try {
                FlatType filterType = FlatType.valueOf(flatTypeFilter);
                projectStream = projectStream.filter(p -> p.getFlatTypes().contains(filterType));
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Invalid flat type filter value: " + flatTypeFilter);
            }
        }

        return projectStream.collect(Collectors.toList());
    }

    /**
     * Creates a new project with initial settings.
     *
     * @param managerNRIC NRIC of the managing manager
     * @param projectName Name of the project
     * @param location Project location
     * @param startDate Application opening date
     * @param endDate Application closing date
     * @param officerSlots Number of available officer slots
     * @param visible Visibility status
     */
    @Override
    public void createProject(String managerNRIC, String projectName, 
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

        ProjectRepository.add(project);
    }

    /**
     * Updates basic project information.
     *
     * @param project The project to update
     * @param location the new location
     * @param startDate the new application opening date
     * @param endDate the new application closing date
     */
    @Override
    public void updateProject(Project project, String location,
                                     LocalDateTime startDate, LocalDateTime endDate) {
        project.setLocation(location);
        project.setApplicationOpenDate(startDate);
        project.setApplicationCloseDate(endDate);
        ProjectRepository.update(project);
    }

    @Override
    public void updateProjectDetails(Project project) {
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
    @Override
    public void deleteProject(String projectName) {
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
    @Override
    public void toggleProjectVisibility(String projectName, boolean visible) {
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
    @Override
    public List<Project> getAllOfficersProjects(String officerNRIC) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officerNRIC))
                .collect(Collectors.toList());
    }
}