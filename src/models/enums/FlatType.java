package models.enums;

/**
 * Enum representing different types of flats.
 */
public enum FlatType {
    /**
     * 2-room flat.
     */
    TWO_ROOM ("2 Room"),

    /**
     * 3-room flat.
     */
    THREE_ROOM ("3 Room");

    private final String description;

    /**
     * Constructor for the FlatType enum.
     *
     * @param description the description of the flat type.
     */
    FlatType(String description) {
        this.description = description;
    }

    /**
     * Gets the description of the flat type.
     *
     * @return the description of the flat type.
     */
    public String getDescription() {
        return description;
    }
}