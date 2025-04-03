package controllers;

import models.Project;
import repositories.ProjectRepository;
import services.ProjectService;
import views.ProjectView;
import java.util.List;

public class ProjectController {
    public ProjectController() {

    }

    public void testingProjects() {
        System.out.println(ProjectRepository.getAll().size() + "BA1");
        Project copyProj = ProjectRepository.getAll().get(0);
        ProjectRepository.remove(copyProj.getProjectId());
        System.out.println(ProjectRepository.getAll().size() + "BA2");
        ProjectRepository.add(copyProj);
        System.out.println(ProjectRepository.getAll().size() + "BA3");
    }
}
