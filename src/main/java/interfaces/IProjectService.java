package interfaces;

import java.time.LocalDateTime;
import java.util.List;

import models.Manager;
import models.Officer;
import models.Project;

/**
 * Interface for managing project-related operations.
 */
public interface IProjectService {
    List<Project> getAllProjects();
    List<Project> getVisibleProjects();
    Project getProjectByName(String projectName);
    Project getProjectByOfficer(Officer officer);
    List<Project> getProjectsByManager(Manager manager);
    void createProject(String managerNRIC, String projectName, String location, LocalDateTime startDate, LocalDateTime endDate, int officerSlots, boolean visible);
    void updateProject(Project project, String location, LocalDateTime startDate, LocalDateTime endDate);
    void updateProjectDetails(Project project);
    void deleteProject(String projectName);
    void toggleProjectVisibility(String projectName, boolean visible);
    List<Project> getAllOfficersProjects(String officerNRIC);
    // Method from original IProjectService - keep or remove based on usage?
    // boolean hasOfficerSlots(Project project); 
    // List<Project> getHandledProjects(Officer officer);
}
