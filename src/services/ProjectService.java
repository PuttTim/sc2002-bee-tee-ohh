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
        return project.getAvailableOfficerSlots() >= 0;
    }

    @Override
    public List<Project> findHandledProjects(Officer officer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findHandledProjects'");
    }
}
