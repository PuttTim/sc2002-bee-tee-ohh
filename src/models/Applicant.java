package models;

import java.util.ArrayList;
import java.util.List;

import models.enums.MaritalStatus;
import models.enums.Role;
import models.enums.FlatType;

/**
 * Represents an applicant.
 * <p>The applicant can:</p>
 * <ul>
 *     <li>Apply for projects</li>
 *     <li>Book flats</li>
 *     <li>Manage their project applications</li>
 * </ul>
 */
public class Applicant extends User {
    private List<String> appliedProjects;
    private FlatType bookedFlatType;
    private String bookedProjectId;

    /**
     * Constructs an Applicant object with given details.
     *
     * @param applicantNRIC the applicant's NRIC.
     * @param name the applicant's name.
     * @param password the applicant's password.
     * @param age the applicant's age.
     * @param appliedProjects the list of project IDs of projects the applicant has applied to.
     */
    public Applicant(String applicantNRIC, String name, String password, int age, List<String> appliedProjects) {
        super(applicantNRIC, name, password, age);
        super.setMaritalStatus(MaritalStatus.SINGLE);
        super.setRole(Role.APPLICANT);
        this.appliedProjects = appliedProjects != null ? appliedProjects : new ArrayList<>();
        this.bookedFlatType = null;
        this.bookedProjectId = null;
    }

    // Getters
    /**
     * Gets a list of project IDs of projects the applicant has applied to.
     *
     * @return the list of applied project IDs.
     */
    public List<String> getAppliedProjects() {
        return new ArrayList<>(appliedProjects);
    }

    /**
     * Gets the type of flat the applicant has booked.
     *
     * @return the booked flat type.
     */
    public FlatType getBookedFlatType() {
        return bookedFlatType;
    }

    /**
     * Gets the project ID of the booked flat.
     *
     * @return the booked project ID.
     */
    public String getBookedProjectId() {
        return bookedProjectId;
    }

    /**
     * Checks if the applicant has already booked a flat.
     *
     * @return <code>true</code> if the applicant has booked a flat, <code>false</code> if not.
     */
    public boolean hasBookedFlat() {
        return bookedProjectId != null && bookedFlatType != null;
    }

    // Setters
    /**
     * Sets a list of project IDs of projects the applicant has applied to.
     *
     * @param projects the list of applied project IDs.
     */
    public void setAppliedProjects(List<String> projects) {
        this.appliedProjects = projects != null ? new ArrayList<>(projects) : new ArrayList<>();
    }

    /**
     * Sets the booked flat for the applicant.
     *
     * @param projectId the project ID of the booked flat.
     * @param flatType the type of flat booked.
     */
    public void setBookedFlat(String projectId, FlatType flatType) {
        this.bookedProjectId = projectId;
        this.bookedFlatType = flatType;
    }

    // Helpers
    /**
     * Adds a project ID to a list of applied projects.
     *
     * @param projectId the project ID to be added.
     */
    public void addAppliedProject(String projectId) {
        if (projectId != null && !appliedProjects.contains(projectId)) {
            appliedProjects.add(projectId);
        }
    }

    /**
     * Removes a project ID from a list of applied projects.
     *
     * @param projectId the project ID to be removed.
     */
    public void removeAppliedProject(String projectId) {
        appliedProjects.remove(projectId);
    }

    /**
     * Clears a booked flat's details.
     */
    public void clearBookedFlat() {
        this.bookedProjectId = null;
        this.bookedFlatType = null;
    }
}
