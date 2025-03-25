package models;

public class Applicant extends User {
    private String applicantID;
    private String nric;
    private int age;
    private String maritalStatus;
    
    public Applicant(String applicantID, String name, String nric, int age, String martialStatus) {
        super(applicantID, name, "Applicant");
        this.applicantID = applicantID;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = martialStatus;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public String getApplicantName() {
        return super.name;
    }

    public String getNric() {
        return nric;
    }

    public int getAge() {
        return age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }
}
