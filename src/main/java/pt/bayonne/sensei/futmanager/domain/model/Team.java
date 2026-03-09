package pt.bayonne.sensei.futmanager.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Team entity representing a football team in the domain model.
 * This is a rich domain entity that encapsulates both data and behavior.
 */
public class Team {
    private final UUID id;
    private final String name;
    private final String country;
    private final Set<Player> players;

    /**
     * Private constructor to enforce creation through factory method.
     * This ensures all invariants are checked during object creation.
     */
    private Team(UUID id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.players = new HashSet<>();
    }

    /**
     * Factory method to create a new Team instance.
     * This method validates all inputs and enforces business rules.
     *
     * @param name the team's name
     * @param country the team's country
     * @return a new Team instance
     * @throws IllegalArgumentException if any of the inputs are invalid
     */
    public static Team create(String name, String country) {
        validateName(name);
        validateCountry(country);

        return new Team(UUID.randomUUID(), name, country);
    }

    /**
     * Adds a player to the team.
     *
     * @param player the player to add
     * @throws IllegalArgumentException if the player is null or already in the team
     */
    public void addPlayer(Player player) {
        validatePlayer(player);
        if (players.contains(player)) {
            throw new IllegalArgumentException("Player is already in the team");
        }
        players.add(player);
    }

    /**
     * Removes a player from the team.
     *
     * @param player the player to remove
     * @throws IllegalArgumentException if the player is null or not in the team
     */
    public void removePlayer(Player player) {
        validatePlayer(player);
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Player is not in the team");
        }
        players.remove(player);
    }

    /**
     * Calculates the team's strength based on the average skill level of all players.
     *
     * @return the team's strength (0.0 if the team has no players)
     */
    public double calculateTeamStrength() {
        if (players.isEmpty()) {
            return 0.0;
        }
        
        return players.stream()
                .mapToInt(Player::getSkillLevel)
                .average()
                .orElse(0.0);
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
     * Validates that the country is not null or empty.
     *
     * @param country the country to validate
     * @throws IllegalArgumentException if the country is null or empty
     */
    private static void validateCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
    }

    /**
     * Validates that the player is not null.
     *
     * @param player the player to validate
     * @throws IllegalArgumentException if the player is null
     */
    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
    }

    /**
     * Gets the team's ID.
     *
     * @return the team's ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the team's name.
     *
     * @return the team's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the team's country.
     *
     * @return the team's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets an unmodifiable view of the team's players.
     *
     * @return an unmodifiable set of the team's players
     */
    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", players=" + players.size() +
                '}';
    }
}