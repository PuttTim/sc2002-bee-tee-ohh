package models;

import models.enums.MaritalStatus;
import models.enums.Role;
import utils.Hash;

/**
 * Represents a user in the system, which can be an applicant/officer/manager.
 *
 * <p>This class contains user details like:</p>
 * <ul>
 *     <li>NRIC</li>
 *     <li>Name</li>
 *     <li>Age</li>
 *     <li>Marital status</li>
 *     <li>Role (applicant/officer/manager)</li>
 *     <li>Password, in hashed format</li>
 * </ul>
 * Allows extension by subclasses.
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
     * Gets the NRIC of the user.
     *
     * @return the NRIC of the user
     */
    public String getUserNRIC() {
        return userNRIC;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user (hashed)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the age of the user.
     *
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the marital status of the user.
     *
     * @return the marital status of the user
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Gets the role of the user (e.g., officer or applicant).
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
