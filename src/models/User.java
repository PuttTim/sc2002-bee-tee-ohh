package models;

public abstract class User {
    protected String userID;
    protected String name;
    protected String role;

    public User(String userID, String name, String role) {
        this.userID = userID;
        this.name = name;
        this.role = role;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
		this.userID = userID;
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getRole() {
		return role;
	}

    public void setRole(String role) {
		this.role = role;
	}
}