package models;

import enums.MaritalStatus;
import enums.Role;

public class Officer extends User {

    public Officer(String officerNric, String name, String password, int age,  Project projectId) {
        super(officerNric, name, password, age, Role.OFFICER, MaritalStatus.SINGLE);
    }
}
