package pt.bayonne.sensei.futmanager.domain.model;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Player entity.
 * Following TDD approach, these tests are written before the actual implementation.
 */
class PlayerTest {

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    @DisplayName("Should create a valid player")
    void shouldCreateValidPlayer() {
        // Arrange
        String name = faker.name().fullName();
        LocalDate dateOfBirth = LocalDate.now().minusYears(faker.number().numberBetween(18, 40));
        String nationality = faker.nation().nationality();
        Position position = Position.MIDFIELDER;
        int skillLevel = faker.number().numberBetween(1, 100);

        // Act
        Player player = Player.create(name, dateOfBirth, nationality, position, skillLevel);

        // Assert
        assertNotNull(player);
        assertNotNull(player.getId());
        assertEquals(name, player.getName());
        assertEquals(dateOfBirth, player.getDateOfBirth());
        assertEquals(nationality, player.getNationality());
        assertEquals(position, player.getPosition());
        assertEquals(skillLevel, player.getSkillLevel());
    }

    @Test
    @DisplayName("Should throw exception when name is null or empty")
    void shouldThrowExceptionWhenNameIsNullOrEmpty() {
        // Arrange
        LocalDate dateOfBirth = LocalDate.now().minusYears(faker.number().numberBetween(18, 40));
        String nationality = faker.nation().nationality();
        Position position = Position.MIDFIELDER;
        int skillLevel = faker.number().numberBetween(1, 100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(null, dateOfBirth, nationality, position, skillLevel));
        
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create("", dateOfBirth, nationality, position, skillLevel));
    }

    @Test
    @DisplayName("Should throw exception when date of birth is null or in the future")
    void shouldThrowExceptionWhenDateOfBirthIsNullOrInFuture() {
        // Arrange
        String name = faker.name().fullName();
        String nationality = faker.nation().nationality();
        Position position = Position.MIDFIELDER;
        int skillLevel = faker.number().numberBetween(1, 100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(name, null, nationality, position, skillLevel));
        
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(name, LocalDate.now().plusDays(1), nationality, position, skillLevel));
    }

    @Test
    @DisplayName("Should throw exception when nationality is null or empty")
    void shouldThrowExceptionWhenNationalityIsNullOrEmpty() {
        // Arrange
        String name = faker.name().fullName();
        LocalDate dateOfBirth = LocalDate.now().minusYears(faker.number().numberBetween(18, 40));
        Position position = Position.MIDFIELDER;
        int skillLevel = faker.number().numberBetween(1, 100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(name, dateOfBirth, null, position, skillLevel));
        
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(name, dateOfBirth, "", position, skillLevel));
    }

    @Test
    @DisplayName("Should throw exception when position is null")
    void shouldThrowExceptionWhenPositionIsNull() {
        // Arrange
        String name = faker.name().fullName();
        LocalDate dateOfBirth = LocalDate.now().minusYears(faker.number().numberBetween(18, 40));
        String nationality = faker.nation().nationality();
        int skillLevel = faker.number().numberBetween(1, 100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(name, dateOfBirth, nationality, null, skillLevel));
    }

    @Test
    @DisplayName("Should throw exception when skill level is out of range")
    void shouldThrowExceptionWhenSkillLevelIsOutOfRange() {
        // Arrange
        String name = faker.name().fullName();
        LocalDate dateOfBirth = LocalDate.now().minusYears(faker.number().numberBetween(18, 40));
        String nationality = faker.nation().nationality();
        Position position = Position.MIDFIELDER;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(name, dateOfBirth, nationality, position, 0));
        
        assertThrows(IllegalArgumentException.class, () -> 
            Player.create(name, dateOfBirth, nationality, position, 101));
    }

    @Test
    @DisplayName("Should calculate age correctly")
    void shouldCalculateAgeCorrectly() {
        // Arrange
        String name = faker.name().fullName();
        LocalDate dateOfBirth = LocalDate.now().minusYears(25);
        String nationality = faker.nation().nationality();
        Position position = Position.MIDFIELDER;
        int skillLevel = faker.number().numberBetween(1, 100);

        // Act
        Player player = Player.create(name, dateOfBirth, nationality, position, skillLevel);

        // Assert
        assertEquals(25, player.getAge());
    }

    @Test
    @DisplayName("Should update skill level correctly")
    void shouldUpdateSkillLevelCorrectly() {
        // Arrange
        String name = faker.name().fullName();
        LocalDate dateOfBirth = LocalDate.now().minusYears(faker.number().numberBetween(18, 40));
        String nationality = faker.nation().nationality();
        Position position = Position.MIDFIELDER;
        int initialSkillLevel = 50;
        
        Player player = Player.create(name, dateOfBirth, nationality, position, initialSkillLevel);
        
        // Act
        player.updateSkillLevel(75);

        // Assert
        assertEquals(75, player.getSkillLevel());
    }

    @Test
    @DisplayName("Should throw exception when updating skill level out of range")
    void shouldThrowExceptionWhenUpdatingSkillLevelOutOfRange() {
        // Arrange
        String name = faker.name().fullName();
        LocalDate dateOfBirth = LocalDate.now().minusYears(faker.number().numberBetween(18, 40));
        String nationality = faker.nation().nationality();
        Position position = Position.MIDFIELDER;
        int initialSkillLevel = 50;
        
        Player player = Player.create(name, dateOfBirth, nationality, position, initialSkillLevel);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> player.updateSkillLevel(0));
        assertThrows(IllegalArgumentException.class, () -> player.updateSkillLevel(101));
    }
}