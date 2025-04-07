package models;

import enums.MaritalStatus;
import enums.Role;

public class User {
    protected String userNric;
    protected String name;
    protected String password;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected Role role;

    public User(String userNric, String name, String password, int age) {
        this.userNric = userNric;
        this.name = name;
        this.password = password;
        this.age = age;
    }

    // Getters
    public String getUserNric() {
        return userNric;
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
        // TODO: Hash the password
        this.password = password;
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