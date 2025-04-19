package interfaces;

import java.util.List;

import models.Project;

public interface IProjectView {
    public void showProjectList(List<Project> projects);
    public void displayEmptyMessage();
    public void displayProjectDetails(Project project);
}
