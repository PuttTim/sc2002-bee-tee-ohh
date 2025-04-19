package controllers;

import java.util.List;
import java.util.stream.Collectors;

import models.Application;
import models.Officer;
import models.Project;
import models.Registration;
import models.enums.RegistrationStatus;
import repositories.ApplicationRepository;
import repositories.OfficerRepository;
import repositories.RegistrationRepository;
import services.*;
import views.*;

import services.OfficerService;
import services.ProjectService;

import views.CommonView;

public class OfficerController {
    //register to join project as officer
    public static void registerToHandleProject(Officer officer) {
        List<Project> projects = ProjectService.getVisibleProjects();
        List<Registration> officerRegistrations = RegistrationRepository.getByOfficer(officer);
        List<Project> registeredProjects = projects.stream()
                .filter(p -> officerRegistrations.stream().filter(r -> r.getRegistrationStatus() != RegistrationStatus.REJECTED).anyMatch(r -> r.getProjectID().equals(p.getProjectID())))
                .collect(Collectors.toList());
        if (projects.isEmpty()) {
            CommonView.displayMessage("No projects available for registration.");
            return;
        }

        int projectChoice = -1;

        while (true) {
            ProjectView.displayOfficerRegistrations(projects, officerRegistrations, officer);
            projectChoice = CommonView.promptInt("Select project number (or 0 to cancel): ", 0, projects.size());

            if (projectChoice == 0) {
                break; // Cancel registration
            }

            Project project = projects.get(projectChoice - 1);
            if (project.getOfficerSlots() <= project.getOfficers().size()) {
                CommonView.displayError("No available slots for this project.");
                continue;
            }

            if (ApplicationRepository.hasApplication(officer, project.getProjectID())) {
                CommonView.displayError("You have an existing BTO application for this project.");
                continue;
            }

            if (officerRegistrations.stream().anyMatch(r -> r.getProjectID().equals(project.getProjectID()))) {
                CommonView.displayError("You have an existing registration for this project.");
                continue; 
            }
            
            if (registeredProjects.stream().anyMatch(r -> r.getApplicationOpenDate().isBefore(project.getApplicationCloseDate()) &&
                    r.getApplicationCloseDate().isAfter(project.getApplicationOpenDate()))) {
                CommonView.displayError("You have an existing registration that overlaps with this project's timeline.");
                continue; 
            }

            break;
        }

        System.out.println("Selected project: " + (projectChoice != 0 ? projects.get(projectChoice - 1).getProjectName() : "None"));

        if (projectChoice != 0) {
            Project selectedProject = projects.get(projectChoice - 1);
            Registration registration = new Registration(officer, selectedProject.getProjectID());
            RegistrationRepository.add(registration);
            CommonView.displayMessage("You have successfully registered to handle project: " + selectedProject.getProjectName() + " Please wait for approval.");
        } else {
            CommonView.displayMessage("Registration cancelled.");
        }
    }

    public static void checkHandlerRegistration(Officer officer) {
        if (OfficerRepository.hasExistingProject(officer)) {
            Project project = ProjectService.getProjectByOfficer(officer);
            if (project != null) {
                CommonView.displayMessage("You are currently handling project: " + project.getProjectName());
            }
        } else if (OfficerService.hasExistingRegistration(officer)) {
            CommonView.displayMessage("You have a pending registration request.");
        } else {
            CommonView.displayMessage("You have no active registrations.");
        }
    }

    //view details of project
    public static void viewHandledProjectDetails(Officer officer) {
        List<Project> projects = ProjectService.getAllOfficersProjects(officer.getUserNRIC());
        if (projects != null && !projects.isEmpty()) {
            boolean running = true;
            while (running) {
                CommonView.displayHeader("Project Details");
                OfficerView.displayOfficerHandledProjects(projects);

                int projectChoice = CommonView.promptInt("Select project number to view details (or 0 to cancel): ", 0, projects.size());

                if (projectChoice == 0) {
                    CommonView.displayMessage("Cancelled viewing project details.");
                    return;
                }

                Project selectedProject = projects.get(projectChoice - 1);

                CommonView.displayHeader("Project Details for " + selectedProject.getProjectName());
                ProjectView.displayProjectDetailsOfficerView(selectedProject);

                int choice = OfficerView.showSelectHandledProjectMenu(selectedProject);

                switch (choice) {
                    case 1 -> {
                        // View applications
                        // ApplicationView.displayApplications(selectedProject);
                    }
                    case 2 -> {
                        // View enquiries
                        // manageProjectEnquiries(officer);
                    }
                    case 0 -> {
                        CommonView.displayMessage("Cancelled viewing project details.");
                        break;
                    }
                }
            }         

        } else {
            CommonView.displayError("You do not have any projects assigned to you.");
        }
    }

    //view and reply to enquiries
    public static void manageProjectEnquiries(Officer officer) {
        Project project = ProjectService.getProjectByOfficer(officer);
        if (project != null) {
            ProjectController.viewProjectEnquiries(project.getProjectName());
        } else {
            CommonView.displayError("You are not handling any project.");
        }
    }

    //help select flat
    public static void processApplication() {
        //TODO
    }

    //generate receipt
    public static void generateReceipt() {
        //TODO
    }
}
