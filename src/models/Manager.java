package models;

import enums.Role;

public class Manager extends User {
    
    public Manager(String nric, String name, String password) {
        super(nric, name, password, Role.MANAGER);
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
