package pt.bayonne.sensei.futmanager.domain.model;

/**
 * Enum representing the positions a player can have in a football team.
 * This is a value object in the domain model.
 */
public enum Position {
    GOALKEEPER("Goalkeeper"),
    DEFENDER("Defender"),
    MIDFIELDER("Midfielder"),
    FORWARD("Forward");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the position.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a string representation of the position.
     *
     * @return the display name of the position
     */
    @Override
    public String toString() {
        return displayName;
    }
}