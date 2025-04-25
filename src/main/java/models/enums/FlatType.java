package models.enums;

/**
 * <p>Represents the types of flats available.</p>
 * <ul>
 * <li><strong>TWO_ROOM:</strong> A 2-room flat.</li>
 * <li><strong>THREE_ROOM:</strong> A 3-room flat.</li>
 * </ul>
 */
public enum FlatType {
    TWO_ROOM ("2 Room"),
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
