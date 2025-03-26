package models;

import enums.MaritalStatus;

public class Applicant extends User {
//    private int age;
//    private String maritalStatus;
    
    public Applicant(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    // Getters
//    public int getAge() {
//        return age;
//    }

//    public MaritalStatus getMaritalStatus() {
//        return maritalStatus;
//    }

    // Setters
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public void setMaritalStatus(String maritalStatus) {
//        this.maritalStatus = maritalStatus;
//    }

    public void viewAvailableProjects() {
        //TODO
    }

    public void applyForProject() {
        //TODO
    }

    public void submitWithdrawRequest() {
        //TODO
    }

    public void submitEnquiry() {
        //TODO
    }

    public void viewEnquiry() {
        //TODO
    }

    public void editEnquiry() {
        //TODO
    }

    public void deleteEnquiry() {
        //TODO
    }
}
