package services;

import java.util.List;
import java.util.stream.Collectors;

import interfaces.IProjectService;
import models.Officer;
import models.Project;
import repositories.ProjectRepository;

public class ProjectService implements IProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.getAll();
    }

    public List<Project> getVisibleProjects() {
        return projectRepository.getAll().stream()
                .filter(Project::isVisible)
                .collect(Collectors.toList());
    }

    public boolean hasOfficerSlots(Project project) {
        return project.getOfficers().size() < project.getOfficerSlots();
    }

    @Override
    public List<Project> findHandledProjects(Officer officer) {
        return projectRepository.getAll().stream()
                .filter(project -> project.getOfficers().contains(officer))
                .collect(Collectors.toList());
    }
}
