package models;

import enums.MaritalStatus;
import enums.Role;

public class Applicant extends User {

    public Applicant(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password, Role.APPLICANT);
    }
}
