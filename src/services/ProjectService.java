package services;

import java.util.List;
import java.util.stream.Collectors;
import models.Officer;
import models.Project;
import repositories.ProjectRepository;

public class ProjectService {
    public static List<Project> getAllProjects() {
        return ProjectRepository.getAll();
    }

    public static List<Project> getVisibleProjects() {
        return ProjectRepository.getAll().stream()
                .filter(Project::isVisible)
                .collect(Collectors.toList());
    }

    public static boolean hasOfficerSlots(Project project) {
        return project.getOfficers().size() < project.getOfficerSlots();
    }

    public static List<Project> getHandledProjects(Officer officer) {
        return ProjectRepository.getAll().stream()
                .filter(project -> project.getOfficers().contains(officer.getUserNRIC()))
                .collect(Collectors.toList());
    }

    public static Project getProjectByOfficer(Officer officer) {
        return ProjectRepository.getAll().stream()
                .filter(project -> project.getOfficers().contains(officer.getUserNRIC()))
                .findFirst()
                .orElse(null);
    }
}
