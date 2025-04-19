package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import models.enums.FlatType;

/**
 * Represents a housing project managed by a manager.
 * <p>A project includes details such as:</p>
 * <ul>
 *     <li>Flat types</li>
 *     <li>Application dates</li>
 *     <li>Officer slots</li>
 * </ul>
 */
public class Project {
    private static int lastProjectID = 0;

    private String projectID;
    private String managerNRIC;
    private String projectName;
    private String location;
    private List<FlatType> flatTypes;
    private List<Integer> flatTypeUnits;
    private List<Integer> flatTypeSellingPrice;
    private LocalDateTime applicationOpenDate;
    private LocalDateTime applicationCloseDate;
    private int officerSlots;
    private boolean isVisible;
    private boolean visible;
    private List<String> applicants;
    private List<String> officers;

    /**
     * Creates a Project object with the given details and auto-generates a project ID.
     *
     * @param managerNRIC the NRIC of the manager.
     * @param projectName the name of the project.
     * @param location the location of the project.
     * @param flatTypes list of flat types available.
     * @param flatTypeUnits number of units for each flat type.
     * @param flatTypeSellingPrice selling price for each flat type.
     * @param applicationOpenDate date applications open.
     * @param applicationCloseDate date applications close.
     * @param officerSlots number of officer slots.
     * @param isVisible whether the project is visible.
     */
    public Project(String managerNRIC, String projectName, String location,
                  List<FlatType> flatTypes, List<Integer> flatTypeUnits, List<Integer> flatTypeSellingPrice,
                  LocalDateTime applicationOpenDate, LocalDateTime applicationCloseDate,
                  int officerSlots, boolean isVisible) {
        this.projectID = "P" + (++Project.lastProjectID);
        this.managerNRIC = managerNRIC;
        this.projectName = projectName;
        this.location = location;
        this.flatTypes = flatTypes;
        this.flatTypeUnits = flatTypeUnits;
        this.flatTypeSellingPrice = flatTypeSellingPrice;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.officerSlots = officerSlots;
        this.isVisible = isVisible;
        this.applicants = new ArrayList<>();
        this.officers = new ArrayList<>();
    }

    /**
     * Creates a Project object with basic information, for search or display.
     *
     * @param projectID the ID of the project.
     * @param managerNRIC the manager's NRIC.
     * @param projectName the name of the project.
     * @param location the location of the project.
     * @param applicationOpenDate when applications open.
     * @param applicationCloseDate when applications close.
     * @param officerSlots number of officer slots.
     * @param visible visibility flag.
     */
    public Project(String projectID, String managerNRIC, String projectName, String location,
                  LocalDateTime applicationOpenDate, LocalDateTime applicationCloseDate,
                  int officerSlots, boolean visible) {
        this.projectID = projectID;
        this.managerNRIC = managerNRIC;
        this.projectName = projectName;
        this.location = location;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.officerSlots = officerSlots;
        this.visible = visible;
    }

    // This constructor is used when loading from repository
    /**
     * Creates a full Project object, used when loading from repository.
     *
     * @param projectID the ID of the project.
     * @param managerNRIC the manager's NRIC.
     * @param projectName the name of the project.
     * @param location the location of the project.
     * @param flatTypes list of flat types.
     * @param flatTypeUnits unit counts for each type.
     * @param flatTypeSellingPrice selling prices for each type.
     * @param applicationOpenDate when applications open.
     * @param applicationCloseDate when applications close.
     * @param officerSlots number of officer slots.
     * @param isVisible visibility flag.
     * @param applicants list of applicant NRICs.
     * @param officers list of officer NRICs.
     */
    public Project(String projectID, String managerNRIC, String projectName, String location,
                  List<FlatType> flatTypes, List<Integer> flatTypeUnits, List<Integer> flatTypeSellingPrice,
                  LocalDateTime applicationOpenDate, LocalDateTime applicationCloseDate,
                  int officerSlots, boolean isVisible,
                  List<String> applicants, List<String> officers) {
        this.projectID = projectID;
        this.managerNRIC = managerNRIC;
        this.projectName = projectName;
        this.location = location;
        this.flatTypes = flatTypes;
        this.flatTypeUnits = flatTypeUnits;
        this.flatTypeSellingPrice = flatTypeSellingPrice;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.officerSlots = officerSlots;
        this.isVisible = isVisible;
        this.applicants = applicants != null ? applicants : new ArrayList<>();
        this.officers = officers != null ? officers : new ArrayList<>();

        try {
            int numericId = Integer.parseInt(projectID.replaceAll("\\D+", ""));
            if (numericId > Project.lastProjectID) {
                Project.lastProjectID = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // Getters
    /**
     * Gets the project ID.
     *
     * @return the project ID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the manager's NRIC.
     *
     * @return the manager's NRIC
     */
    public String getManagerNRIC() {
        return managerNRIC;
    }

    /**
     * Gets the project name.
     *
     * @return the name of the project
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the project location.
     *
     * @return the location of the project
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the list of flat types.
     *
     * @return a list of flat types
     */
    public List<FlatType> getFlatTypes() {
        return new ArrayList<>(flatTypes);
    }

    /**
     * Gets the list of unit counts for each flat type.
     *
     * @return a list of unit counts
     */
    public List<Integer> getFlatTypeUnits() {
        return new ArrayList<>(flatTypeUnits);
    }

    /**
     * Gets the list of selling prices for each flat type.
     *
     * @return a list of selling prices
     */
    public List<Integer> getFlatTypeSellingPrice() {
        return new ArrayList<>(flatTypeSellingPrice);
    }

    /**
     * Gets the application opening date.
     *
     * @return the date when applications open
     */
    public LocalDateTime getApplicationOpenDate() {
        return applicationOpenDate;
    }

    /**
     * Gets the application closing date.
     *
     * @return the date when applications close
     */
    public LocalDateTime getApplicationCloseDate() {
        return applicationCloseDate;
    }

    /**
     * Gets the number of officer slots.
     *
     * @return number of officer slots
     */
    public int getOfficerSlots() {
        return officerSlots;
    }

    /**
     * Checks if the project is visible.
     *
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Gets the internal visibility flag.
     *
     * @return true if visible, false otherwise
     */
    public boolean isVisibleFlag() {
        return visible;
    }

    /**
     * Gets the list of applicants' NRICs.
     *
     * @return a list of applicant NRICs
     */
    public List<String> getApplicants() {
        return new ArrayList<>(applicants);
    }

    /**
     * Gets the list of officers' NRICs.
     *
     * @return a list of officer NRICs
     */
    public List<String> getOfficers() {
        return officers != null ? officers : new ArrayList<>();
    }

    // Setters
    /**
     * Sets the manager's NRIC.
     *
     * @param managerNRIC the manager's NRIC
     */
    public void setManagerNRIC(String managerNRIC) {
        this.managerNRIC = managerNRIC;
    }

    /**
     * Sets the project name.
     *
     * @param projectName the name of the project
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Sets the project location.
     *
     * @param location the location of the project
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the list of flat types.
     *
     * @param flatTypes list of flat types
     */
    public void setFlatTypes(List<FlatType> flatTypes) {
        this.flatTypes = new ArrayList<>(flatTypes);
    }

    /**
     * Sets the unit count for each flat type.
     *
     * @param flatTypeUnits list of unit counts
     */
    public void setFlatTypeUnits(List<Integer> flatTypeUnits) {
        this.flatTypeUnits = new ArrayList<>(flatTypeUnits);
    }

    /**
     * Sets the selling price for each flat type.
     *
     * @param flatTypeSellingPrice list of selling prices
     */
    public void setFlatTypeSellingPrice(List<Integer> flatTypeSellingPrice) {
        this.flatTypeSellingPrice = new ArrayList<>(flatTypeSellingPrice);
    }

    /**
     * Sets the application open date.
     *
     * @param applicationOpenDate the open date
     */
    public void setApplicationOpenDate(LocalDateTime applicationOpenDate) {
        this.applicationOpenDate = applicationOpenDate;
    }

    /**
     * Sets the application close date.
     *
     * @param applicationCloseDate the close date
     */
    public void setApplicationCloseDate(LocalDateTime applicationCloseDate) {
        this.applicationCloseDate = applicationCloseDate;
    }

    /**
     * Sets the number of officer slots.
     *
     * @param officerSlots number of officer slots
     */
    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }

    /**
     * Sets whether the project is visible.
     *
     * @param isVisible true if visible, false otherwise
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Sets the internal visibility flag.
     *
     * @param visible true if visible, false otherwise
     */
    public void setVisibleFlag(boolean visible) {
        this.visible = visible;
    }

    // Helpers
    /**
     * Checks if applications are currently open.
     *
     * @param now the current date and time
     * @return true if within the application window, false otherwise
     */
    public boolean isApplicationOpen(LocalDateTime now) {
        return now.isAfter(applicationOpenDate) && now.isBefore(applicationCloseDate);
    }

    /**
     * Adds an applicant to the project.
     *
     * @param applicantNRIC NRIC of the applicant
     */
    public void addApplicant(String applicantNRIC) {
        if (!applicants.contains(applicantNRIC)) {
            applicants.add(applicantNRIC);
        }
    }

    /**
     * Adds an officer to the project.
     *
     * @param officerNRIC NRIC of the officer
     */
    public void addOfficer(String officerNRIC) {
        if (officers == null) {
            officers = new ArrayList<>();
        }
        officers.add(officerNRIC);
    }

    /**
     * Removes an officer from the project.
     *
     * @param officerNRIC NRIC of the officer to remove
     */
    public void removeOfficer(String officerNRIC) {
        if (officers != null) {
            officers.remove(officerNRIC);
        }
    }

    /**
     * Decreases the number of officer slots by 1.
     */
    public void reduceOfficerSlot() {
        if (officerSlots > 0) {
            officerSlots--;
        }
    }

    /**
     * Removes an applicant from the project.
     *
     * @param applicantNRIC NRIC of the applicant to remove
     */
    public void removeApplicant(String applicantNRIC) {
        applicants.remove(applicantNRIC);
    }

    /**
     * Reduces the available unit count for a flat type by 1.
     *
     * @param type the flat type to reduce
     */
    public void reduceFlatCount(FlatType type) {
        int index = flatTypes.indexOf(type);
        if (index != -1 && flatTypeUnits.get(index) > 0) {
            flatTypeUnits.set(index, flatTypeUnits.get(index) - 1);
        }
    }

    /**
     * Gets the index of a flat type.
     *
     * @param type the flat type
     * @return the index or -1 if not found
     */
    public int getFlatTypeIndex(FlatType type) {
        return flatTypes.indexOf(type);
    }

    /**
     * Gets the available unit count for a flat type.
     *
     * @param type the flat type
     * @return the number of available units
     */
    public int getAvailableUnits(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeUnits.get(index) : 0;
    }

    /**
     * Gets the selling price of a flat type.
     *
     * @param type the flat type
     * @return the selling price, or 0 if not found
     */
    public int getFlatPrice(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeSellingPrice.get(index) : 0;
    }
}