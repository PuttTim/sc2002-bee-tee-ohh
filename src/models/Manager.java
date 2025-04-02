package models;

import enums.MaritalStatus;
import enums.Role;

public class Manager extends User {
    
    public Manager(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password, Role.MANAGER);
    }

    // Getters
    @Override
    public String getName(){
        return super.getName();
    }

    // Setters
    @Override
    public void setName(String name){
        if (name != null && !name.trim().isEmpty()) {
            super.setName(name);
        } else {
            throw new IllegalArgumentException("Manager's name cannot be empty.");
        }
    }
}
