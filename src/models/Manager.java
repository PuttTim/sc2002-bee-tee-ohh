package models;

public class Manager extends User {
    private String managerID;
    private String nric;

    public Manager(String managerID, String name, String nric) {
        super(managerID, name, "Officer");
        this.managerID = managerID;
        this.nric = nric;
    }

    public String getManagerID() {
        return managerID;
    }

    public String getManagerName() {
        return super.name;
    }

    public String getNric() {
        return nric;
    }
}
