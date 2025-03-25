package models;

import enums.Role;

public class Applicant extends User {
    private int age;
    private String maritalStatus;
    
    public Applicant(String nric, String name, String password, int age, String martialStatus) {
        super(nric, name, password, Role.APPLICANT);
        this.age = age;
        this.maritalStatus = martialStatus;
    }

    // Getters
    public int getAge() {
        return age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    // Setters
    public void setAge(int age) {
        this.age = age;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}
