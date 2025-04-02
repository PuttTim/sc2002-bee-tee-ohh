package models;

import enums.MaritalStatus;
import enums.Role;
import enums.MaritalStatus;

public abstract class User {
    protected String nric;
    protected String name;
    protected String password;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected Role role;

    public User(String nric, String name, String password, int age, MaritalStatus maritalStatus, Role role) {
        this.nric = nric;
        this.name = name;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
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

    public String getPassword() {
        return password;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
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

    public void setPassword(String password) {
        this.password = password;   // Hash password here
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