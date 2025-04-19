package interfaces;

import java.util.List;

import models.Officer;
import models.Project;

public interface IProjectService {
    public boolean hasOfficerSlots(Project project);
    public List<Project> getHandledProjects(Officer officer);

}
