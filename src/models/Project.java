package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import models.enums.FlatType;
import views.CommonView;

/**
 * Represents a housing project managed by a manager with various flat types and applicants.
 *
 * <p>This class handles project details:</p>
 * <ul>
 *     <li>Project ID</li>
 *     <li>Manager's NRIC</li>
 *     <li>Flat details</li>
 *     <li>Application details</li>
 *     <li>Officer slots</li>
 *     <li>Visibility statuses</li>
 *     <li>The applicants and officers</li>
 *     <li>Methods for managing the project</li>
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
     * Constructs a new Project with the specified details, including flat types, units, and selling prices.
     *
     * @param managerNRIC the NRIC of the manager responsible for the project
     * @param projectName the name of the project
     * @param location the location of the project
     * @param flatTypes a list of flat types available in the project
     * @param flatTypeUnits a list of units available for each flat type
     * @param flatTypeSellingPrice a list of selling prices for each flat type
     * @param applicationOpenDate the date the application period opens
     * @param applicationCloseDate the date the application period closes
     * @param officerSlots the number of officer slots available for the project
     * @param isVisible the visibility status of the project
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
     * Constructs a new Project with the specified details, used for loading from repository.
     *
     * @param projectID the ID of the project
     * @param managerNRIC the NRIC of the manager responsible for the project
     * @param projectName the name of the project
     * @param location the location of the project
     * @param applicationOpenDate the date the application period opens
     * @param applicationCloseDate the date the application period closes
     * @param officerSlots the number of officer slots available for the project
     * @param visible the visibility status of the project
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

    /**
     * Constructs a new Project with all details, used for repository loading.
     *
     * @param projectID the ID of the project
     * @param managerNRIC the NRIC of the manager responsible for the project
     * @param projectName the name of the project
     * @param location the location of the project
     * @param flatTypes a list of flat types available in the project
     * @param flatTypeUnits a list of units available for each flat type
     * @param flatTypeSellingPrice a list of selling prices for each flat type
     * @param applicationOpenDate the date the application period opens
     * @param applicationCloseDate the date the application period closes
     * @param officerSlots the number of officer slots available for the project
     * @param isVisible the visibility status of the project
     * @param applicants the list of applicants for the project
     * @param officers the list of officers for the project
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
     * Gets the unique ID of the project.
     *
     * @return the project ID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the NRIC of the manager responsible for the project.
     *
     * @return the manager's NRIC
     */
    public String getManagerNRIC() {
        return managerNRIC;
    }

    /**
     * Gets the name of the project.
     *
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the location of the project.
     *
     * @return the project location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets a copy of the list of flat types available in the project.
     *
     * @return a list of flat types
     */
    public List<FlatType> getFlatTypes() {
        return new ArrayList<>(flatTypes);
    }

    /**
     * Gets a copy of the list of available units for each flat type in the project.
     *
     * @return a list of available units for each flat type
     */
    public List<Integer> getFlatTypeUnits() {
        return new ArrayList<>(flatTypeUnits);
    }

    /**
     * Gets a copy of the list of selling prices for each flat type in the project.
     *
     * @return a list of selling prices for each flat type
     */
    public List<Integer> getFlatTypeSellingPrice() {
        return new ArrayList<>(flatTypeSellingPrice);
    }

    /**
     * Gets the date and time when the application period opens.
     *
     * @return the application open date and time
     */
    public LocalDateTime getApplicationOpenDate() {
        return applicationOpenDate;
    }

    /**
     * Gets the date and time when the application period closes.
     *
     * @return the application close date and time
     */
    public LocalDateTime getApplicationCloseDate() {
        return applicationCloseDate;
    }

    /**
     * Gets the number of officer slots available for the project.
     *
     * @return the number of officer slots
     */
    public int getOfficerSlots() {
        return officerSlots;
    }

    /**
     * Gets the visibility status of the project.
     *
     * @return true if the project is visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Gets the visibility flag for the project.
     *
     * @return true if the project is visible, false otherwise
     */
    public boolean isVisibleFlag() {
        return visible;
    }

    /**
     * Gets a copy of the list of applicants for the project.
     *
     * @return a list of applicants' NRICs
     */
    public List<String> getApplicants() {
        return new ArrayList<>(applicants);
    }

    /**
     * Gets the list of officers assigned to the project.
     *
     * @return a list of officers' NRICs
     */
    public List<String> getOfficers() {
        return officers != null ? officers : new ArrayList<>();
    }

// Setters

    /**
     * Sets the NRIC of the manager responsible for the project.
     *
     * @param managerNRIC the manager's NRIC
     */
    public void setManagerNRIC(String managerNRIC) {
        this.managerNRIC = managerNRIC;
    }

    /**
     * Sets the name of the project.
     *
     * @param projectName the project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Sets the location of the project.
     *
     * @param location the project location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the list of flat types available in the project.
     *
     * @param flatTypes a list of flat types
     */
    public void setFlatTypes(List<FlatType> flatTypes) {
        this.flatTypes = new ArrayList<>(flatTypes);
    }

    /**
     * Sets the list of available units for each flat type in the project.
     *
     * @param flatTypeUnits a list of available units for each flat type
     */
    public void setFlatTypeUnits(List<Integer> flatTypeUnits) {
        this.flatTypeUnits = new ArrayList<>(flatTypeUnits);
    }

    /**
     * Sets the list of selling prices for each flat type in the project.
     *
     * @param flatTypeSellingPrice a list of selling prices for each flat type
     */
    public void setFlatTypeSellingPrice(List<Integer> flatTypeSellingPrice) {
        this.flatTypeSellingPrice = new ArrayList<>(flatTypeSellingPrice);
    }

    /**
     * Sets the date and time when the application period opens.
     *
     * @param applicationOpenDate the application open date and time
     */
    public void setApplicationOpenDate(LocalDateTime applicationOpenDate) {
        this.applicationOpenDate = applicationOpenDate;
    }

    /**
     * Sets the date and time when the application period closes.
     *
     * @param applicationCloseDate the application close date and time
     */
    public void setApplicationCloseDate(LocalDateTime applicationCloseDate) {
        this.applicationCloseDate = applicationCloseDate;
    }

    /**
     * Sets the number of officer slots available for the project.
     *
     * @param officerSlots the number of officer slots
     */
    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }

    /**
     * Sets the visibility status of the project.
     *
     * @param isVisible the visibility status
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Sets the visibility flag for the project.
     *
     * @param visible the visibility flag
     */
    public void setVisibleFlag(boolean visible) {
        this.visible = visible;
    }


    /**
     * Checks if the application period for the project is currently open.
     *
     * @param now the current date and time
     * @return true if the application period is open, false otherwise
     */
    public boolean isApplicationOpen(LocalDateTime now) {
        return now.isAfter(applicationOpenDate) && now.isBefore(applicationCloseDate);
    }

    /**
     * Adds an applicant to the project.
     *
     * @param applicantNRIC the NRIC of the applicant
     */
    public void addApplicant(String applicantNRIC) {
        if (!applicants.contains(applicantNRIC)) {
            applicants.add(applicantNRIC);
        }
    }

    /**
     * Adds an officer to the project.
     *
     * @param officerNRIC the NRIC of the officer
     */
    public void addOfficer(String officerNRIC) {
        if (!officers.contains(officerNRIC)) {
            officers.add(officerNRIC);
        } else {
            CommonView.displayMessage("Officer " + officerNRIC + " already exists in project " + projectID + ".");
        }
    }

    /**
     * Removes an officer from the project.
     *
     * @param officerNRIC the NRIC of the officer to be removed
     */
    public void removeOfficer(String officerNRIC) {
        if (officers != null) {
            officers.remove(officerNRIC);
        }
    }

    /**
     * Reduces the available officer slots by one.
     */
    public void reduceOfficerSlot() {
        if (officerSlots > 0) {
            officerSlots--;
        }
    }

    /**
     * Removes an applicant from the project.
     *
     * @param applicantNRIC the NRIC of the applicant to be removed
     */
    public void removeApplicant(String applicantNRIC) {
        applicants.remove(applicantNRIC);
    }

    /**
     * Reduces the available units for a given flat type by one.
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
     * Retrieves the index of a given flat type.
     *
     * @param type the flat type to search for
     * @return the index of the flat type, or -1 if not found
     */
    public int getFlatTypeIndex(FlatType type) {
        return flatTypes.indexOf(type);
    }

    /**
     * Retrieves the number of available units for a given flat type.
     *
     * @param type the flat type to check
     * @return the number of available units for the flat type
     */
    public int getAvailableUnits(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeUnits.get(index) : 0;
    }

    /**
     * Retrieves the selling price of a given flat type.
     *
     * @param type the flat type to get the price for
     * @return the selling price of the flat type
     */
    public int getFlatPrice(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeSellingPrice.get(index) : 0;
    }
}
