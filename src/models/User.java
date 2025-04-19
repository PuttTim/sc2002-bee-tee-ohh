package models;

import models.enums.MaritalStatus;
import models.enums.Role;
import utils.Hash;

/**
 * Represents a system user.
 * <p>A user has:</p>
 * <ul>
 *     <li>NRIC</li>
 *     <li>Name</li>
 *     <li>Password</li>
 *     <li>Age</li>
 *     <li>Marital status</li>
 *     <li>Role</li>
 * </ul>
 */
public class User {
    protected String userNRIC;
    protected String name;
    protected String password;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected Role role;

    /**
     * Creates a User object with the given details.
     *
     * @param userNRIC the NRIC of the user
     * @param name the name of the user
     * @param password the password of the user (will be hashed later)
     * @param age the age of the user
     */
    public User(String userNRIC, String name, String password, int age) {
        this.userNRIC = userNRIC;
        this.name = name;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.age = age;
    }

    // Getters
    /**
     * @return the NRIC of the user
     */
    public String getUserNRIC() {
        return userNRIC;
    }

    /**
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the marital status of the user
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @return the role of the user
     */
    public Role getRole() {
        return role;
    }

    // Setters
    /**
     * Sets the password for the user.
     * The password will be hashed before saving.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = Hash.hash(password);
    }

    /**
     * Sets the age of the user.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the marital status of the user.
     *
     * @param maritalStatus the new marital status
     */
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the new role
     */
    public void setRole(Role role) {
        this.role = role;
    }
}