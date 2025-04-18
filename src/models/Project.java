package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import models.enums.FlatType;

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
    public String getProjectID() {
        return projectID;
    }

    public String getManagerNRIC() {
        return managerNRIC;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getLocation() {
        return location;
    }

    public List<FlatType> getFlatTypes() {
        return new ArrayList<>(flatTypes);
    }

    public List<Integer> getFlatTypeUnits() {
        return new ArrayList<>(flatTypeUnits);
    }

    public List<Integer> getFlatTypeSellingPrice() {
        return new ArrayList<>(flatTypeSellingPrice);
    }

    public LocalDateTime getApplicationOpenDate() {
        return applicationOpenDate;
    }

    public LocalDateTime getApplicationCloseDate() {
        return applicationCloseDate;
    }

    public int getOfficerSlots() {
        return officerSlots;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isVisibleFlag() {
        return visible;
    }

    public List<String> getApplicants() {
        return new ArrayList<>(applicants);
    }

    public List<String> getOfficers() {
        return officers != null ? officers : new ArrayList<>();
    }

    // Setters
    public void setManagerNRIC(String managerNRIC) {
        this.managerNRIC = managerNRIC;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFlatTypes(List<FlatType> flatTypes) {
        this.flatTypes = new ArrayList<>(flatTypes);
    }

    public void setFlatTypeUnits(List<Integer> flatTypeUnits) {
        this.flatTypeUnits = new ArrayList<>(flatTypeUnits);
    }

    public void setFlatTypeSellingPrice(List<Integer> flatTypeSellingPrice) {
        this.flatTypeSellingPrice = new ArrayList<>(flatTypeSellingPrice);
    }

    public void setApplicationOpenDate(LocalDateTime applicationOpenDate) {
        this.applicationOpenDate = applicationOpenDate;
    }

    public void setApplicationCloseDate(LocalDateTime applicationCloseDate) {
        this.applicationCloseDate = applicationCloseDate;
    }

    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setVisibleFlag(boolean visible) {
        this.visible = visible;
    }

    // Helpers
    public boolean isApplicationOpen(LocalDateTime now) {
        return now.isAfter(applicationOpenDate) && now.isBefore(applicationCloseDate);
    }

    public void addApplicant(String applicantNRIC) {
        if (!applicants.contains(applicantNRIC)) {
            applicants.add(applicantNRIC);
        }
    }

    public void addOfficer(String officerNRIC) {
        if (officers == null) {
            officers = new ArrayList<>();
        }
        officers.add(officerNRIC);
    }

    public void removeOfficer(String officerNRIC) {
        if (officers != null) {
            officers.remove(officerNRIC);
        }
    }

    public void reduceOfficerSlot() {
        if (officerSlots > 0) {
            officerSlots--;
        }
    }

    public void removeApplicant(String applicantNRIC) {
        applicants.remove(applicantNRIC);
    }

    public void reduceFlatCount(FlatType type) {
        int index = flatTypes.indexOf(type);
        if (index != -1 && flatTypeUnits.get(index) > 0) {
            flatTypeUnits.set(index, flatTypeUnits.get(index) - 1);
        }
    }

    public int getFlatTypeIndex(FlatType type) {
        return flatTypes.indexOf(type);
    }

    public int getAvailableUnits(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeUnits.get(index) : 0;
    }

    public int getFlatPrice(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeSellingPrice.get(index) : 0;
    }
}