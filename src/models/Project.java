package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.enums.FlatType;
import views.CommonView;

public class Project {
    private static int lastProjectID = 0;

    private String projectID;
    private String managerNRIC;
    private String projectName;
    private String location;
    private Map<FlatType, Integer> flatTypeToUnit;
    private Map<FlatType, Integer> flatTypeToSellingPrice;
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
        this.flatTypeToUnit = new HashMap<>();
        this.flatTypeToSellingPrice = new HashMap<>();
        for (int i = 0; i < flatTypes.size(); i++) {
            this.flatTypeToUnit.put(flatTypes.get(i), flatTypeUnits.get(i));
            this.flatTypeToSellingPrice.put(flatTypes.get(i), flatTypeSellingPrice.get(i));
        }
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.officerSlots = officerSlots;
        this.isVisible = isVisible;
        this.applicants = new ArrayList<>();
        this.officers = new ArrayList<>();
    }

    public Project(String managerNRIC, String projectName, String location,
                  LocalDateTime applicationOpenDate, LocalDateTime applicationCloseDate,
                  int officerSlots, boolean visible) {
        this.projectID = "P" + (++lastProjectID);
        this.managerNRIC = managerNRIC;
        this.projectName = projectName;
        this.location = location;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.officerSlots = officerSlots;
        this.isVisible = visible;
        this.applicants = new ArrayList<>();
        this.officers = new ArrayList<>();
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
        this.flatTypeToUnit = new HashMap<>();
        this.flatTypeToSellingPrice = new HashMap<>();
        for (int i = 0; i < flatTypes.size(); i++) {
            this.flatTypeToUnit.put(flatTypes.get(i), flatTypeUnits.get(i));
            this.flatTypeToSellingPrice.put(flatTypes.get(i), flatTypeSellingPrice.get(i));
        }
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
        return new ArrayList<>(flatTypeToUnit.keySet());
    }

    public List<Integer> getFlatTypeUnits() {
        return new ArrayList<>(flatTypeToUnit.values());
    }

    public List<Integer> getFlatTypeSellingPrice() {
        return new ArrayList<>(flatTypeToSellingPrice.values());
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
        this.flatTypeToUnit.clear();
        for (int i = 0; i < flatTypes.size(); i++) {
            this.flatTypeToUnit.put(flatTypes.get(i), flatTypeToUnit.get(flatTypes.get(i)));
        }
    }

    public void setFlatTypeUnits(List<Integer> flatTypeUnits) {
        int i = 0;
        for (FlatType flatType : flatTypeToUnit.keySet()) {
            this.flatTypeToUnit.put(flatType, flatTypeUnits.get(i++));
        }
    }

    public void setFlatTypeSellingPrice(List<Integer> flatTypeSellingPrice) {
        int i = 0;
        for (FlatType flatType : flatTypeToSellingPrice.keySet()) {
            this.flatTypeToSellingPrice.put(flatType, flatTypeSellingPrice.get(i++));
        }
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

    public void addFlatType(FlatType type, int units, int price) {
        this.flatTypeToUnit.put(type, this.flatTypeToUnit.getOrDefault(type, 0) + units);
        this.flatTypeToSellingPrice.put(type, price);
    }

    public void addOfficer(String officerNRIC) {
        // System.out.println("Adding officer: " + officerNRIC);
        if (!officers.contains(officerNRIC)) {
            // System.out.println("Adding officer: 3 " + officerNRIC);
            // officers.forEach(System.out::println);
            try {
                officers.add(officerNRIC);
            } catch (Exception e) {
            }
            // CommonView.displayMessage("Officer " + officerNRIC + " added to project " + projectID + ".");
        } else {
            CommonView.displayMessage("Officer " + officerNRIC + " already exists in project " + projectID + ".");
        }
        // System.out.println("Adding officer: 2 " + officerNRIC);

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
        if (!flatTypeToUnit.containsKey(type)) {
            throw new IllegalArgumentException("Flat type not found in project");
        }
        flatTypeToUnit.put(type, flatTypeToUnit.get(type) - 1);
    }

    public void incrementFlatCount(FlatType flatType) {
        if (!flatTypeToUnit.containsKey(flatType)) {
            throw new IllegalArgumentException("Flat type not found in project");
        }
        flatTypeToUnit.put(flatType, flatTypeToUnit.get(flatType) + 1);
    }

    public int getFlatTypeIndex(FlatType type) {
        List<FlatType> flatTypes = getFlatTypes();
        return flatTypes.indexOf(type);
    }

    public int getAvailableUnits(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeToUnit.get(type) : 0;
    }

    public int getFlatPrice(FlatType type) {
        int index = getFlatTypeIndex(type);
        return index != -1 ? flatTypeToSellingPrice.get(type) : 0;
    }
}