package models;

import enums.Role;

public class Officer extends User {
    private Project project;

    public Officer(String nric, String name, String password, Project project) {
        super(nric, name, password, Role.OFFICER);
        this.project = null;
    }

    // Getters
    public Project getProject() {
        return project;
    }

    // Setters
    public void setProject(Project project) {
        this.project = project; // Check this again
    }
}
