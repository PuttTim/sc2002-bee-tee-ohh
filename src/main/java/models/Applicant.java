package models;

import java.util.ArrayList;
import java.util.List;

import models.enums.MaritalStatus;
import models.enums.Role;
import models.enums.FlatType;

/**
 * Represents an applicant in the housing system.
 * <p>
 * This class handles the details of an applicant, including:
 * <ul>
 *   <li>Managing applied projects and booked flats</li>
 *   <li>Tracking the status of applied projects</li>
 *   <li>Allowing the addition and removal of projects from the applicant's application list</li>
 * </ul>
 */

public class Applicant extends User {
    private List<String> appliedProjects;
    private FlatType bookedFlatType;
    private String bookedProjectId;

    /**
     * <p>Creates a new applicant.</p>
     * @param applicantNRIC The NRIC of the applicant.
     * @param name The name of the applicant.
     * @param password The password for the applicant's account.
     * @param age The age of the applicant.
     * @param appliedProjects A list of projects the applicant has applied for.
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
     * <p>Returns a copy of the list of projects the applicant has applied to.</p>
     * @return A list of applied project IDs.
     */
    public List<String> getAppliedProjects() {
        return new ArrayList<>(appliedProjects);
    }

    /**
     * <p>Returns the type of flat the applicant has booked (if any).</p>
     * @return The booked flat type.
     */
    public FlatType getBookedFlatType() {
        return bookedFlatType;
    }

    /**
     * <p>Returns the project ID associated with the booked flat (if any).</p>
     * @return The booked project ID.
     */
    public String getBookedProjectId() {
        return bookedProjectId;
    }

    /**
     * <p>Checks if the applicant has booked a flat.</p>
     * @return True if the applicant has booked a flat, false otherwise.
     */
    public boolean hasBookedFlat() {
        return bookedProjectId != null && bookedFlatType != null;
    }

    // Setters

    /**
     * <p>Sets the list of projects the applicant has applied to.</p>
     * @param projects A list of project IDs.
     */
    public void setAppliedProjects(List<String> projects) {
        this.appliedProjects = projects != null ? new ArrayList<>(projects) : new ArrayList<>();
    }

    /**
     * <p>Sets the booked project and flat type for the applicant.</p>
     * @param projectId The project ID of the booked project.
     * @param flatType The type of flat the applicant booked.
     */
    public void setBookedFlat(String projectId, FlatType flatType) {
        this.bookedProjectId = projectId;
        this.bookedFlatType = flatType;
    }

    // Helpers

    /**
     * <p>Adds a project ID to the list of applied projects.</p>
     * @param projectId The project ID to add.
     */
    public void addAppliedProject(String projectId) {
        if (appliedProjects == null) {
            appliedProjects = new ArrayList<>();
        }
        if (projectId != null && !appliedProjects.contains(projectId)) {
            List<String> appliedProjectsCopy = new ArrayList<>(this.appliedProjects);
            appliedProjectsCopy.add(projectId);
            setAppliedProjects(appliedProjectsCopy);
        }

    }

    /**
     * <p>Removes a project ID from the list of applied projects.</p>
     * @param projectId The project ID to remove.
     */
    public void removeAppliedProject(String projectId) {
        appliedProjects.remove(projectId);
    }

    /**
     * <p>Clears the booked flat details.</p>
     */
    public void clearBookedFlat() {
        this.bookedProjectId = null;
        this.bookedFlatType = null;
    }
}
