package controllers;

import models.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ManagerController {
    private List<Project> projects = new ArrayList<>(); // will projects be stored in another class or this class

    // create a project (where will a new project be stored?)
    public void createProject(String projectID, Manager manager, String projectName, String location, String applicationStart, String applicationEnd, int officerSlots, boolean visibility){
        Project project = new Project(projectID, manager, projectName, location, LocalDate.parse(applicationStart), LocalDate.parse(applicationEnd), officerSlots, visibility);
        projects.add(project);
    }

    // edit a project
    public void editProject(Project project, String projectName, String location, String applicationStart, String applicationEnd){
        project.setProjectName(projectName);
        project.setLocation(location);
        project.setApplicationStart(LocalDate.parse(applicationStart));
        project.setApplicationEnd(LocalDate.parse(applicationEnd));
    }

    // delete a project
    public void deleteProject(Project project){
        projects.remove(project);
    }

    // toggle visibility of a project
    public void toggleVisibility(Project project, boolean visibility){
        project.setVisibility(visibility);
    }

    // view all projects, regardless of visibility setting
    public void viewAllProjects(){
        // list each project in the Project array
    }

    // filter and view lists of projects created by a manager

    // view pending and approved Officer registration

    // approve or reject Officer registrations
    public void approveOfficerRegistration(Project project, boolean approve){
        if(approve){
            project.setOfficerSlots(project.getOfficerSlots()-1);
            // add the officer and the registration to the list of approved registrations
        }
        else{
            System.out.println("Registration rejected");
        }
    }
    // approve or reject applicant registrations

    // approve or reject applicant registration withdrawal

    // generate a report of list of applicants with their respective registrations

    // view enquiries of all projects

    // view and reply to enquiries regarding the project a manager is handling
}
