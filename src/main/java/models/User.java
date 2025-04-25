package models;

import models.enums.MaritalStatus;
import models.enums.Role;

import utils.Hash;

public class User {
    protected String userNRIC;
    protected String name;
    protected String password;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected Role role;

    public User(String userNRIC, String name, String password, int age) {
        this.userNRIC = userNRIC;
        this.name = name;
        this.password = password;
        this.age = age;
    }

    // Getters
    public String getUserNRIC() {
        return userNRIC;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public Role getRole() {
        return role;
    }

    // Setters
    public void setPassword(String password) {
        this.password = Hash.hash(password);
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}