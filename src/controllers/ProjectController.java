package controllers;

import models.Applicant;
import models.Filter;
import models.Project;
import models.Officer;
import services.ProjectService;
import views.ProjectView;
import views.AuthView;
import views.CommonView;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProjectController {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void viewAvailableProjects(Applicant applicant) {
        List<Project> projects = ProjectService.getVisibleProjects();

        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available.");
            return;
        }

        ProjectView.displayAvailableProjects(projects);

        boolean continueProjectMenu = true;
        while (continueProjectMenu) {
            int menuChoice = ProjectView.getProjectMenuChoice();

            switch (menuChoice) {
                case 1:
                    continueProjectMenu = false;
                    break;

                case 2: {
                    int projectChoice = ProjectView.getProjectChoice(projects);
                    ProjectView.displayProjectDetails(projects.get(projectChoice));
                    break;
                }

                case 3: {
                    EnquiryController.createNewEnquiry(applicant);
                    break;
                }

                default:
                    CommonView.displayError("Please enter a valid number!");
                    break;
            }
        }
    }

    public static void createProject() {
        String projectId = ProjectView.getProjectId();
        String managerNRIC = AuthView.getNRIC();
        String projectName = ProjectView.getProjectName();
        String location = ProjectView.getProjectLocation();
        
        try {
            String startDateStr = AuthView.getApplicationDate("start");
            String endDateStr = AuthView.getApplicationDate("end");
            
            LocalDateTime startDate = LocalDate.parse(startDateStr, dateFormatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(endDateStr, dateFormatter).atStartOfDay();
            
            int officerSlots = ProjectView.getOfficerSlots();
            boolean visibility = ProjectView.getProjectVisibility();

            ProjectService.createProject(projectId, managerNRIC, projectName, location, 
                startDate, endDate, officerSlots, visibility);
            ProjectView.displayProjectCreationSuccess(projectName);
        } catch (Exception e) {
            CommonView.displayError("Error creating project: " + e.getMessage());
        }
    }

    public static void editProject() {
        String projectName = ProjectView.getProjectName();
        Project project = ProjectService.getProjectByName(projectName);
        
        if (project == null) {
            ProjectView.displayProjectNotFound();
            return;
        }

        String location = ProjectView.getProjectLocation();
        try {
            String startDateStr = AuthView.getApplicationDate("start");
            String endDateStr = AuthView.getApplicationDate("end");
            
            LocalDateTime startDate = LocalDate.parse(startDateStr, dateFormatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(endDateStr, dateFormatter).atStartOfDay();

            ProjectService.updateProject(project, location, startDate, endDate);
            ProjectView.displayProjectUpdateSuccess();
        } catch (Exception e) {
            CommonView.displayError("Error updating project: " + e.getMessage());
        }
    }

    public static void deleteProject() {
        String projectName = ProjectView.getProjectName();
        ProjectService.deleteProject(projectName);
        ProjectView.displayProjectDeleteSuccess();
    }

    public static void toggleProjectVisibility() {
        String projectName = ProjectView.getProjectName();
        boolean visibility = ProjectView.getProjectVisibility();
        ProjectService.toggleProjectVisibility(projectName, visibility);
        ProjectView.displayVisibilityUpdateSuccess();
    }

    public static void viewAllProjects() {
        List<Project> projects = ProjectService.getAllProjects();
        ProjectView.displayAvailableProjects(projects);
    }

    public static void viewProjectEnquiries(String projectName) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project != null) {
            EnquiryController.viewProjectEnquiries(project);
        } else {
            ProjectView.displayProjectNotFound();
        }
    }

    public static void viewOfficerRegistrations() {
        List<Project> projects = ProjectService.getAllProjects();
        ProjectView.displayOfficerRegistrations(projects);
    }

    public static void handleOfficerRegistration(Officer officer, String projectName) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project == null) {
            ProjectView.displayProjectNotFound();
            return;
        }
        
        if (!ProjectService.hasOfficerSlots(project)) {
            CommonView.displayError("Project has no more officer slots.");
            return;
        }

        ProjectService.addOfficerToProject(project, officer.getUserNRIC());
        CommonView.displaySuccess("Officer registration submitted successfully.");
    }

    public static void removeOfficerFromProject(String projectName, String officerNRIC) {
        Project project = ProjectService.getProjectByName(projectName);
        if (project != null) {
            ProjectService.removeOfficerFromProject(project, officerNRIC);
        } else {
            ProjectView.displayProjectNotFound();
        }
    }
}
