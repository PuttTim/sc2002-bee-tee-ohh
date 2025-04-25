package models;

import models.enums.MaritalStatus;
import models.enums.Role;
import utils.Hash;

/**
 * Represents a user in the system, which can be an applicant or an officer.
 * <p>
 * This class contains personal details about the user, including their NRIC, name,
 * age, marital status, role, and password. The password is stored in a hashed format for security.
 * </p>
 */
public class User {
    protected String userNRIC;
    protected String name;
    protected String password;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected Role role;

    /**
     * Constructs a new user with the provided details.
     *
     * @param userNRIC the NRIC of the user
     * @param name the name of the user
     * @param password the password for the user (hashed before storage)
     * @param age the age of the user
     */
    public User(String userNRIC, String name, String password, int age) {
        this.userNRIC = userNRIC;
        this.name = name;
        this.password = password;
        this.age = age;
    }

    // Getters

    /**
     * Retrieves the NRIC of the user.
     *
     * @return the NRIC of the user
     */
    public String getUserNRIC() {
        return userNRIC;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user (hashed)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the age of the user.
     *
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * Retrieves the marital status of the user.
     *
     * @return the marital status of the user
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Retrieves the role of the user (e.g., officer or applicant).
     *
     * @return the role of the user
     */
    public Role getRole() {
        return role;
    }

    // Setters

    /**
     * Sets the password for the user. The password is hashed before storing.
     *
     * @param password the new password for the user
     */
    public void setPassword(String password) {
        this.password = Hash.hash(password);
    }

    /**
     * Sets the age of the user.
     *
     * @param age the new age of the user
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the marital status of the user.
     *
     * @param maritalStatus the new marital status of the user
     */
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the new role of the user (e.g., officer or applicant)
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
