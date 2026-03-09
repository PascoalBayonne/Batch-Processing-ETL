/**
 * Persistence adapter package containing persistence adapters (repository implementations).
 * 
 * This package contains persistence adapters that implement the repository interfaces
 * defined in the domain layer. Persistence adapters are responsible for data access and
 * persistence operations, such as CRUD operations on the database.
 * 
 * Persistence adapters:
 * - Implement repository interfaces defined in the domain layer
 * - Use Spring Data JPA or JDBC for database operations
 * - Translate between domain objects and database entities
 * - Handle database-specific concerns (e.g., transactions, caching)
 * 
 * Examples of persistence adapters in a football manager application might include:
 * - JpaPlayerRepository: Implements PlayerRepository using Spring Data JPA
 * - JpaTeamRepository: Implements TeamRepository using Spring Data JPA
 * - JpaMatchRepository: Implements MatchRepository using Spring Data JPA
 */
package pt.bayonne.sensei.futmanager.infrastructure.adapter.persistence;