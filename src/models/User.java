package models;

import enums.Role;

public abstract class User {
    private String nric;
    private String name;
    private String password;
    private Role role;

    public User(String nric, String name, String password, Role role) {
        this.nric = nric;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getNric() {
        return nric;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
            return password;
    }

    public Role getRole() {
		return role;
	}

    // Setters
    public void setNric(String nric) {
		this.nric = nric;
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;   // Hash password
    }

    public void setRole(Role role) {
		this.role = role;
	}
}