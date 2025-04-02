package models;

import enums.MaritalStatus;
import enums.Role;

public class Manager extends User {
    
    public Manager(String managerNric, String name, String password, int age) {
        super(managerNric, name, password, age, Role.MANAGER, MaritalStatus.SINGLE);
    }
}
