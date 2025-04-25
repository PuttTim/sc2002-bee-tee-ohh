package models.enums;

/**
 * <p>Represents the types of flats available.</p>
 */
public enum FlatType {
    /**
     * A 2-room flat.
     */
    TWO_ROOM ("2 Room"),

    /**
     * A 3-room flat.
     */
    THREE_ROOM ("3 Room");

    private final String description;

    FlatType(String description) {
        this.description = description;
    }

    /**
     * <p>Gets the description of the flat type.</p>
     * @return The description of the flat type.
     */
    public String getDescription() {
        return description;
    }
}
