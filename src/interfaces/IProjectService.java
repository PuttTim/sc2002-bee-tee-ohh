package interfaces;

import models.Officer;
import models.Project;

import java.util.List;

public interface IProjectService {
    public boolean hasOfficerSlots(Project project);
    public List<Project> findHandledProjects(Officer officer);

}
