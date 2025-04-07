package models;

import java.util.ArrayList;
import java.util.List;

import enums.MaritalStatus;
import enums.Role;

public class Applicant extends Officer {
    private List<String> appliedProjectIds;

    public Applicant(String applicantNric, String name, String password, int age, List<String> appliedProjectIds) {
        super(applicantNric, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.APPLICANT);
        this.appliedProjectIds = appliedProjectIds != null ? appliedProjectIds : new ArrayList<>();
    }

    // Getter
    public List<String> getAppliedProjectIds() {
        return appliedProjectIds;
    }

    // Setter
    public void setAppliedProjectIds(List<String> projectIds) {
        this.appliedProjectIds = projectIds != null ? projectIds : new ArrayList<>();
    }

    // Helper
    public void addAppliedProject(String projectId) {
        if (!appliedProjectIds.contains(projectId)) {
            appliedProjectIds.add(projectId);
        }
    }
}