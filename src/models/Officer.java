package models;

import enums.MaritalStatus;
import enums.Role;

public class Officer extends User {

    public Officer(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password, Role.OFFICER);
    }
}
