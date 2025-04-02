package models;

import enums.MaritalStatus;
import enums.Role;

public class Applicant extends User {
    
    public Applicant(String applicantNric, String name, String password, int age) {
        super(applicantNric, name, password, age, Role.APPLICANT, MaritalStatus.SINGLE);
    }
}
