package interfaces;

import models.Project;

import java.util.List;

public interface IProjectView {
    public void showProjectList(List<Project> projects);
    public void displayEmptyMessage();
    public void displayProjectDetails(Project project);
}
