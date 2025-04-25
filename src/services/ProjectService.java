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

    public static Project getProjectByName(String projectName) {
        return ProjectRepository.getByName(projectName);
    }

    public static Project getProjectByOfficer(Officer officer) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officer.getUserNRIC()))
                .findFirst()
                .orElse(null);
    }

    public static List<Project> getProjectsByManager(Manager manager) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getManagerNRIC().equals(manager.getUserNRIC()))
                .collect(Collectors.toList());
    }

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

    public static void deleteProject(String projectName) {
        Project project = getProjectByName(projectName);
        if (project != null) {
            ProjectRepository.remove(project);
        } else {
            throw new IllegalArgumentException("Project with name " + projectName + " not found.");
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

    public static List<Project> getAllOfficersProjects(String officerNRIC) {
        return ProjectRepository.getAll().stream()
                .filter(p -> p.getOfficers().contains(officerNRIC))
                .collect(Collectors.toList());
    }
}
