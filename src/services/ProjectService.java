package services;

import models.Project;
import models.Officer;
import repositories.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    public static List<Project> getAllProjects() {
        return ProjectRepository.getAll();
    }

    public static List<Project> getVisibleProjects() {
        return ProjectRepository.getAll().stream()
                .filter(Project::isVisible)
                .collect(Collectors.toList());
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
        project.setApplicationStartDate(startDate);
        project.setApplicationEndDate(endDate);
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
