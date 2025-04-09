package models;

import java.util.ArrayList;
import java.util.List;

import enums.MaritalStatus;
import enums.Role;

public class Applicant extends Officer {
    private List<String> appliedProjects;

    public Applicant(String applicantNRIC, String name, String password, int age, List<String> appliedProjects) {
        super(applicantNRIC, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.APPLICANT);
        this.appliedProjects = appliedProjects != null ? appliedProjects : new ArrayList<>();
    }

    // Getter
    public List<String> getAppliedProjects() {
        return appliedProjects;
    }

    // Setter
    public void setAppliedProjects(List<String> projects) {
        this.appliedProjects = projects != null ? projects : new ArrayList<>();
    }

    // Helper
    public void addAppliedProject(String projectId) {
        if (projectId != null && !appliedProjects.contains(projectId)) {
            appliedProjects.add(projectId);
        }
    }
}
