package models;

import java.util.ArrayList;
import java.util.List;

import enums.MaritalStatus;
import enums.Role;

public class Applicant extends User {
    private List<String> appliedProjectIds;

    public Applicant(String applicantNric, String name, String password, int age, List<String> appliedProjectIds) {
        super(applicantNric, name, password, age, MaritalStatus.SINGLE, Role.APPLICANT);
        this.appliedProjectIds = appliedProjectIds != null ? appliedProjectIds : new ArrayList<>();
    }


    // Getters
    public List<String> getAppliedProjectIds() {
        return appliedProjectIds;
    }

    // Setters
    public void setAppliedProjectIds(List<String> appliedProjectIds) {
        this.appliedProjectIds = appliedProjectIds != null ? appliedProjectIds : new ArrayList<>();
    }

    // Helpers
    public void addAppliedProject(String projectId) {
        if (!appliedProjectIds.contains(projectId)) {
            appliedProjectIds.add(projectId);
        }
    }
}