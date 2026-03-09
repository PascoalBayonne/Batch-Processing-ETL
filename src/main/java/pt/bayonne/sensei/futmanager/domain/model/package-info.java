/**
 * Domain model package containing entities, value objects, and aggregates.
 * 
 * This package contains the core domain model classes that represent the business concepts
 * and their relationships in the football manager application. These classes encapsulate
 * the business rules and logic related to their respective domain concepts.
 * 
 * The domain model is organized into:
 * - Entities: Objects with identity that have a lifecycle (e.g., Player, Team, Match)
 * - Value Objects: Immutable objects that describe characteristics (e.g., Name, Email, Address)
 * - Aggregates: Clusters of entities and value objects treated as a single unit
 * 
 * Following DDD principles, these classes are:
 * - Anemic-free: They contain both data and behavior
 * - Invariant-protected: They enforce business rules and maintain consistency
 * - Framework-independent: They don't depend on any external frameworks
 */
package pt.bayonne.sensei.futmanager.domain.model;