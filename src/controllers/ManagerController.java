package controllers;

import models.*;
import enums.MaritalStatus;
import enums.Role;
import stores.Database;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ManagerController {

    // create a BTO project as a manager
    public void createProject(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner sc = new Scanner(System.in);
        // get projectID
        System.out.println("Enter the ID of the Project:");
        String projectId = sc.nextLine();
        // get project manager NRIC
        System.out.println("Enter the NRIC of the Project manager:");
        String nric = sc.nextLine();
        // get project manager name
        System.out.println("Enter the name of the Project manager:");
        String managerName = sc.nextLine();
        // get Project manager age
        System.out.println("Enter the age of Project manager:");
        int managerAge = sc.nextInt();
        // get Project manager marital status
        System.out.println("Enter Project manager marital status (0 = SINGLE / 1 = MARRIED):");
        int inputMS = sc.nextInt();
        MaritalStatus managerMS;
        while (true){
            if(inputMS != 0 && inputMS != 1){
                System.out.println("Invalid input, enter (0 = SINGLE / 1 = MARRIED):");
                continue;
            }
            else if(inputMS == 0){
                managerMS = MaritalStatus.SINGLE;
                break;
            }
            else if(inputMS == 1){
                managerMS = MaritalStatus.MARRIED;
                break;
            }
        }
        // get project manager password
        System.out.println("Enter the password of the Project manager:");
        String password = sc.nextLine();
        Manager manager = new Manager(nric, managerName, managerAge, managerMS, password);
        // get project name
        System.out.println("Enter the name of the Project:");
        String projectName = sc.nextLine();
        // get project location
        System.out.println("Enter the Project location:");
        String location = sc.nextLine();
        // get project start date
        LocalDate startDate;
        while (true) {
            System.out.print("Enter the Project start date in (dd/mm/yyyy):");
            String startDateInput = sc.nextLine().trim();
            
            // check if user entered the date in the correct format using regex
            if (!startDateInput.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Invalid format! Please use dd/mm/yyyy (e.g 25/12/2023)");
                continue;
            }
            try {
                // parse the input into a LocalDate object
                startDate = LocalDate.parse(startDateInput, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Please enter a valid date (e.g 25/12/2023)");
            }
        }
        // get project end date
        LocalDate endDate;
        while (true) {
            System.out.print("Enter the Project end date in (dd/mm/yyyy):");
            String endDateInput = sc.nextLine().trim();
            
            // check if user entered the date in the correct format using regex
            if (!endDateInput.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Invalid format! Please use dd/mm/yyyy (e.g 25/12/2023)");
                continue;
            }
            try {
                // parse the input into a LocalDate object
                endDate = LocalDate.parse(endDateInput, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Please enter a valid date (e.g 25/12/2023)");
            }
        }
        // get Project officer slots
        System.out.println("Enter the number of Officer slots:");
        int officerSlots = sc.nextInt();
        // get Project visibility
        System.out.println("Is the Project visible to applicants? (yes/no):");
        Boolean visibility = false;
        String visInput;
        while (true){
            visInput = sc.nextLine();
            if(visInput != "yes" && visInput != "no"){
                System.out.println("Invalid input! Please enter (yes/no):");
                continue;
            }
            visibility = true;
            break;
        }

        Project newProject = new Project(projectId, manager, projectName, location, startDate, endDate, officerSlots, visibility);
        // store project to database
        databaseStoreProject(newProject);
        System.out.println("Project + \""  + projectName + "\" created successfully.");
    }

    // edit a BTO project as a manager
    public void editProject(String projectName, String location, String applicationStart, String applicationEnd){
        // switch case for user to choose which attribute of the project to edit
    }

    // edit a project (ignore)
    public void editProject(Project project, String projectName, String location, String applicationStart, String applicationEnd){
        project.setProjectName(projectName);
        project.setLocation(location);
        project.setApplicationStart(LocalDate.parse(applicationStart));
        project.setApplicationEnd(LocalDate.parse(applicationEnd));
    }

    // delete a project
    public void deleteProject(String projectName){
        Database db = new Database();
        if(db.databaseRemoveProject(projectName)){
            System.out.println("Project deleted successfully.");
        }
        else{
            System.out.println("Unable to delete, Project not found!");
        }
    }

    // toggle visibility of a project
    public void toggleVisibility(){
        System.out.println("Enter Project name:");
        Scanner sc = new Scanner(System.in);
        String projectName = sc.nextLine();
        Database db = new Database();
        if(db.databaseFindProject(projectName)){
            while (true){
                System.out.println("Make Project visible? (yes/no):");
                String input = sc.nextLine();
                if(input != "yes" && input != "no"){
                    System.out.println("Invalid input! Please enter (yes/no):");
                    continue;
                }
                else if(input == "yes"){
                    databaseSetProjectVisibility(projectName, true);
                    System.out.println("Project is now visible to applicants.");
                    break;
                }
                else if(input == "no"){
                    databaseSetProjectVisibility(projectName, false);
                    System.out.println("Project is now hidden from applicants.");
                    break;
                }
            }
        }
        else{
            System.out.println("Project not found!");
        }
    }

    // view all projects, regardless of visibility setting
    public void viewAllProjects(){
        databaseViewAllProjects(Role.MANAGER);
    }

    // filter and view lists of projects created by a manager

    // view pending and approved Officer registration
    public void viewOfficerRegistrations(){
        // list each approved registrations (for loop)
        // list each pending registrations (for loop)
    }

    // approve or reject Officer registrations
    public void approveOfficerRegistration(Project project, boolean approve){
        if(approve){
            project.setOfficerSlots(project.getOfficerSlots()-1);
            // add the officer and the registration to the list of approved registrations
        }
        else{
            System.out.println("Officer registration rejected");
        }
    }

    // approve or reject applicant registrations
    // TODO
    /* 
    public void approveApplicantRegistration(Project project, boolean approve){
        if(approve){
            if(DatabaseStore.getlocation(project.getLocation()) > 0){ // check if there are available supply of the selected flat
                // add the applicant and the registration to the list of approved registrations
            }
        }
        else{
            System.out.println("Applicant registration rejected");
        }
    }*/

    // approve or reject applicant registration withdrawal
    // TODO

    // generate a report of list of applicants with their respective registrations
    // TODO

    // view enquiries of all projects
    // TODO

    // view and reply to enquiries regarding the project a manager is handling
    // TODO
}
