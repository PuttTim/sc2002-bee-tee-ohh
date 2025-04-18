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

public class ProjectService {
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

    public static List<Project> getAllProjects() {
        return ProjectRepository.getAll();
    }

    public static List<Project> getVisibleProjects() {
        Role userRole = UserRepository.getUserRole();

        switch (userRole) {
            case APPLICANT:
                return ProjectRepository.getAll().stream()
                    .filter(Project::isVisible)
                    .filter(p -> p.getApplicationOpenDate().isBefore(DateTimeUtils.getCurrentDateTime()) 
                        && p.getApplicationCloseDate().isAfter(DateTimeUtils.getCurrentDateTime()))
                    .collect(Collectors.toList());
            case OFFICER:
                return ProjectRepository.getAll().stream()
                    .filter(Project::isVisible)
                    .collect(Collectors.toList());
            case MANAGER:
                return ProjectRepository.getAll();
            default:
                return List.of();
        }



    }

    public static Project getProjectByName(String projectName) {
        return ProjectRepository.getByName(projectName);
    }

    public static Project getProjectByOfficer(Officer officer) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officer.getUserNRIC()))
                .findFirst()
                .orElse(null);
    }

    public static void createProject(String projectId, String managerNRIC, String projectName, 
            String location, LocalDateTime startDate, LocalDateTime endDate, 
            int officerSlots, boolean visible) {
        Project newProject = new Project(projectId, managerNRIC, projectName, location,
                startDate, endDate, officerSlots, visible);
        ProjectRepository.add(newProject);
        ProjectRepository.saveAll();
    }

    public static void updateProject(Project project, String location, 
            LocalDateTime startDate, LocalDateTime endDate) {
        project.setLocation(location);
        project.setApplicationOpenDate(startDate);
        project.setApplicationCloseDate(endDate);
        ProjectRepository.saveAll();
    }

    public static void deleteProject(String projectName) {
        Project project = getProjectByName(projectName);
        if (project != null) {
            ProjectRepository.remove(project);
            ProjectRepository.saveAll();
        }
    }

    public static void toggleProjectVisibility(String projectName, boolean visible) {
        Project project = getProjectByName(projectName);
        if (project != null) {
            project.setVisible(visible);
            ProjectRepository.saveAll();
        }
    }

    public static boolean hasOfficerSlots(Project project) {
        return project != null && project.getOfficerSlots() > 0;
    }

    public static void addOfficerToProject(Project project, String officerNRIC) {
        if (project != null && !project.getOfficers().contains(officerNRIC)) {
            project.addOfficer(officerNRIC);
            project.reduceOfficerSlot();
            ProjectRepository.saveAll();
        }
    }

    public static void removeOfficerFromProject(Project project, String officerNRIC) {
        if (project != null && project.getOfficers().contains(officerNRIC)) {
            project.removeOfficer(officerNRIC);
            ProjectRepository.saveAll();
        }
    }
}
