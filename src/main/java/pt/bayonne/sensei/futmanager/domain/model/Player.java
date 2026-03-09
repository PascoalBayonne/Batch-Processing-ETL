package pt.bayonne.sensei.futmanager.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.UUID;

/**
 * Player entity representing a football player in the domain model.
 * This is a rich domain entity that encapsulates both data and behavior.
 */
public class Player {
    private final UUID id;
    private final String name;
    private final LocalDate dateOfBirth;
    private final String nationality;
    private final Position position;
    private int skillLevel;

    /**
     * Private constructor to enforce creation through factory method.
     * This ensures all invariants are checked during object creation.
     */
    private Player(UUID id, String name, LocalDate dateOfBirth, String nationality, Position position, int skillLevel) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.position = position;
        this.skillLevel = skillLevel;
    }

    /**
     * Factory method to create a new Player instance.
     * This method validates all inputs and enforces business rules.
     *
     * @param name the player's name
     * @param dateOfBirth the player's date of birth
     * @param nationality the player's nationality
     * @param position the player's position
     * @param skillLevel the player's skill level (1-100)
     * @return a new Player instance
     * @throws IllegalArgumentException if any of the inputs are invalid
     */
    public static Player create(String name, LocalDate dateOfBirth, String nationality, Position position, int skillLevel) {
        validateName(name);
        validateDateOfBirth(dateOfBirth);
        validateNationality(nationality);
        validatePosition(position);
        validateSkillLevel(skillLevel);

        return new Player(UUID.randomUUID(), name, dateOfBirth, nationality, position, skillLevel);
    }

    /**
     * Updates the player's skill level.
     *
     * @param skillLevel the new skill level
     * @throws IllegalArgumentException if the skill level is invalid
     */
    public void updateSkillLevel(int skillLevel) {
        validateSkillLevel(skillLevel);
        this.skillLevel = skillLevel;
    }

    /**
     * Calculates the player's age based on the date of birth.
     *
     * @return the player's age in years
     */
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Validates that the name is not null or empty.
     *
     * @param name the name to validate
     * @throws IllegalArgumentException if the name is null or empty
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    /**
     * Validates that the date of birth is not null and not in the future.
     *
     * @param dateOfBirth the date of birth to validate
     * @throws IllegalArgumentException if the date of birth is null or in the future
     */
    private static void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
    }

    /**
     * Validates that the nationality is not null or empty.
     *
     * @param nationality the nationality to validate
     * @throws IllegalArgumentException if the nationality is null or empty
     */
    private static void validateNationality(String nationality) {
        if (nationality == null || nationality.trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality cannot be null or empty");
        }
    }

    /**
     * Validates that the position is not null.
     *
     * @param position the position to validate
     * @throws IllegalArgumentException if the position is null
     */
    private static void validatePosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
    }

    /**
     * Validates that the skill level is between 1 and 100.
     *
     * @param skillLevel the skill level to validate
     * @throws IllegalArgumentException if the skill level is not between 1 and 100
     */
    private static void validateSkillLevel(int skillLevel) {
        if (skillLevel < 1 || skillLevel > 100) {
            throw new IllegalArgumentException("Skill level must be between 1 and 100");
        }
    }

    /**
     * Gets the player's ID.
     *
     * @return the player's ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's date of birth.
     *
     * @return the player's date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the player's nationality.
     *
     * @return the player's nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Gets the player's position.
     *
     * @return the player's position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the player's skill level.
     *
     * @return the player's skill level
     */
    public int getSkillLevel() {
        return skillLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nationality='" + nationality + '\'' +
                ", position=" + position +
                ", skillLevel=" + skillLevel +
                '}';
    }
}