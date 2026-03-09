package pt.bayonne.sensei.futmanager.domain.model;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Team entity.
 * Following TDD approach, these tests are written before the actual implementation.
 */
class TeamTest {

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    @DisplayName("Should create a valid team")
    void shouldCreateValidTeam() {
        // Arrange
        String name = faker.team().name();
        String country = faker.nation().nationality();
        
        // Act
        Team team = Team.create(name, country);

        // Assert
        assertNotNull(team);
        assertNotNull(team.getId());
        assertEquals(name, team.getName());
        assertEquals(country, team.getCountry());
        assertTrue(team.getPlayers().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when name is null or empty")
    void shouldThrowExceptionWhenNameIsNullOrEmpty() {
        // Arrange
        String country = faker.nation().nationality();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            Team.create(null, country));
        
        assertThrows(IllegalArgumentException.class, () -> 
            Team.create("", country));
    }

    @Test
    @DisplayName("Should throw exception when country is null or empty")
    void shouldThrowExceptionWhenCountryIsNullOrEmpty() {
        // Arrange
        String name = faker.team().name();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            Team.create(name, null));
        
        assertThrows(IllegalArgumentException.class, () -> 
            Team.create(name, ""));
    }

    @Test
    @DisplayName("Should add player to team")
    void shouldAddPlayerToTeam() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());
        Player player = createPlayer();

        // Act
        team.addPlayer(player);

        // Assert
        assertEquals(1, team.getPlayers().size());
        assertTrue(team.getPlayers().contains(player));
    }

    @Test
    @DisplayName("Should throw exception when adding null player")
    void shouldThrowExceptionWhenAddingNullPlayer() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            team.addPlayer(null));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate player")
    void shouldThrowExceptionWhenAddingDuplicatePlayer() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());
        Player player = createPlayer();
        team.addPlayer(player);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            team.addPlayer(player));
    }

    @Test
    @DisplayName("Should remove player from team")
    void shouldRemovePlayerFromTeam() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());
        Player player = createPlayer();
        team.addPlayer(player);

        // Act
        team.removePlayer(player);

        // Assert
        assertTrue(team.getPlayers().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when removing null player")
    void shouldThrowExceptionWhenRemovingNullPlayer() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            team.removePlayer(null));
    }

    @Test
    @DisplayName("Should throw exception when removing player not in team")
    void shouldThrowExceptionWhenRemovingPlayerNotInTeam() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());
        Player player = createPlayer();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            team.removePlayer(player));
    }

    @Test
    @DisplayName("Should calculate team strength correctly")
    void shouldCalculateTeamStrengthCorrectly() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());
        
        // Create 11 players with different skill levels
        List<Player> players = IntStream.rangeClosed(1, 11)
            .mapToObj(i -> {
                Player player = createPlayer(50 + i); // Skill levels from 51 to 61
                team.addPlayer(player);
                return player;
            })
            .collect(Collectors.toList());
        
        // Expected average: (51 + 52 + ... + 61) / 11 = 56
        
        // Act
        double teamStrength = team.calculateTeamStrength();

        // Assert
        assertEquals(56.0, teamStrength, 0.01);
    }

    @Test
    @DisplayName("Should return zero strength for empty team")
    void shouldReturnZeroStrengthForEmptyTeam() {
        // Arrange
        Team team = Team.create(faker.team().name(), faker.nation().nationality());
        
        // Act
        double teamStrength = team.calculateTeamStrength();

        // Assert
        assertEquals(0.0, teamStrength, 0.01);
    }

    private Player createPlayer() {
        return createPlayer(faker.number().numberBetween(1, 100));
    }

    private Player createPlayer(int skillLevel) {
        return Player.create(
            faker.name().fullName(),
            LocalDate.now().minusYears(faker.number().numberBetween(18, 40)),
            faker.nation().nationality(),
            Position.values()[faker.number().numberBetween(0, Position.values().length)],
            skillLevel
        );
    }
}