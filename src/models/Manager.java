package models;

import enums.Role;

public class Manager extends User {
    
    public Manager(String nric, String name, String password) {
        super(nric, name, password, Role.MANAGER);
    }
}
