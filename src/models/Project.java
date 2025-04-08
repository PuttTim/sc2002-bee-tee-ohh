package models;

import enums.FlatType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private static int lastProjectID = 0;

    private String projectID;
    private String managerNRIC;
    private String projectName;
    private String location;

    private FlatType flatType1;
    private int flatType1Units;
    private int flatType1SellingPrice;

    private FlatType flatType2;
    private int flatType2Units;
    private int flatType2SellingPrice;

    private LocalDateTime applicationOpenDate;
    private LocalDateTime applicationCloseDate;

    private int officerSlots;
    private boolean isVisible;

    private List<String> applicants;
    private List<String> officers;

    public Project(String managerNRIC, String projectName, String location,
                   FlatType flatType1, int flatType1Units, int flatType1SellingPrice,
                   FlatType flatType2, int flatType2Units, int flatType2SellingPrice,
                   LocalDateTime applicationOpenDate, LocalDateTime applicationCloseDate,
                   int officerSlots, boolean isVisible,
                   List<String> applicants, List<String> officers) {

        this.projectID = "P" + (++Project.lastProjectID);
        this.managerNRIC = managerNRIC;
        this.projectName = projectName;
        this.location = location;
        this.flatType1 = flatType1;
        this.flatType1Units = flatType1Units;
        this.flatType1SellingPrice = flatType1SellingPrice;
        this.flatType2 = flatType2;
        this.flatType2Units = flatType2Units;
        this.flatType2SellingPrice = flatType2SellingPrice;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.officerSlots = officerSlots;
        this.isVisible = isVisible;
        this.applicants = applicants != null ? applicants : new ArrayList<>();
        this.officers = officers != null ? officers : new ArrayList<>();
    }

    public Project(String projectID, String managerNRIC, String projectName, String location,
                   FlatType flatType1, int flatType1Units, int flatType1SellingPrice,
                   FlatType flatType2, int flatType2Units, int flatType2SellingPrice,
                   LocalDateTime applicationOpenDate, LocalDateTime applicationCloseDate,
                   int officerSlots, boolean isVisible,
                   List<String> applicants, List<String> officers) {

        this.projectID = projectID;
        this.managerNRIC = managerNRIC;
        this.projectName = projectName;
        this.location = location;
        this.flatType1 = flatType1;
        this.flatType1Units = flatType1Units;
        this.flatType1SellingPrice = flatType1SellingPrice;
        this.flatType2 = flatType2;
        this.flatType2Units = flatType2Units;
        this.flatType2SellingPrice = flatType2SellingPrice;
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

    public FlatType getFlatType1() {
        return flatType1;
    }

    public int getFlatType1Units() {
        return flatType1Units;
    }

    public int getFlatType1SellingPrice() {
        return flatType1SellingPrice;
    }

    public FlatType getFlatType2() {
        return flatType2;
    }

    public int getFlatType2Units() {
        return flatType2Units;
    }

    public int getFlatType2SellingPrice() {
        return flatType2SellingPrice;
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

    public List<String> getApplicants() {
        return applicants;
    }

    public List<String> getOfficers() {
        return officers;
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

    public void setFlatType1(FlatType flatType1) {
        this.flatType1 = flatType1;
    }

    public void setFlatType1Units(int flatType1Units) {
        this.flatType1Units = flatType1Units;
    }

    public void setFlatType1SellingPrice(int flatType1SellingPrice) {
        this.flatType1SellingPrice = flatType1SellingPrice;
    }

    public void setFlatType2(FlatType flatType2) {
        this.flatType2 = flatType2;
    }

    public void setFlatType2Units(int flatType2Units) {
        this.flatType2Units = flatType2Units;
    }

    public void setFlatType2SellingPrice(int flatType2SellingPrice) {
        this.flatType2SellingPrice = flatType2SellingPrice;
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

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setApplicants(List<String> applicants) {
        this.applicants = applicants != null ? applicants : new ArrayList<>();
    }

    public void setOfficers(List<String> officers) {
        this.officers = officers != null ? officers : new ArrayList<>();
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
        if (!officers.contains(officerNRIC)) {
            officers.add(officerNRIC);
        }
    }

    public void reduceOfficerSlot() {
        if (officerSlots > 0) {
            officerSlots--;
        }
    }

    public void reduceFlatCount(FlatType type) {
        if (type == flatType1 && flatType1Units > 0) {
            flatType1Units--;
        } else if (type == flatType2 && flatType2Units > 0) {
            flatType2Units--;
        }
    }
}