package models;

public class Officer extends User {
    private String officerID;
    private String nric;

    public Officer(String officerID, String name, String nric) {
        super(officerID, name, "Officer");
        this.officerID = officerID;
        this.nric = nric;
    }

    public String getOfficerID() {
        return officerID;
    }

    public String getOfficerName() {
        return super.name;
    }

    public String getNric() {
        return nric;
    }
}
