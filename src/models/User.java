package models;

import enums.Role;
import enums.MaritalStatus;

public abstract class User {
    private String nric;
    private String name;
    private int age;
    private String password;
    private Role role;
    private MaritalStatus maritalStatus;

    public User(String nric, String name, int age, MaritalStatus maritalStatus, String password, Role role) {
        this.nric = nric;
        this.name = name;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public int getAge() {
        return age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
		return role;
	}

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setNric(String nric) {
		this.nric = nric;
	}

    public void setAge(int age) {
        this.age = age;
    }
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setPassword(String password) {
        this.password = password;   // Hash password here
    }

    public void setRole(Role role) {
		this.role = role;
	}
}