/**
 * Domain layer package following Domain-Driven Design principles.
 * 
 * This package contains the core business logic, entities, value objects, aggregates,
 * and domain services that represent the business domain of the football manager application.
 * The domain layer is independent of any external frameworks or technologies and contains
 * the business rules and logic.
 * 
 * The domain layer is organized into:
 * - Entities: Objects with identity that have a lifecycle
 * - Value Objects: Immutable objects that describe characteristics of the domain
 * - Aggregates: Clusters of entities and value objects treated as a single unit
 * - Domain Services: Operations that don't naturally fit within an entity or value object
 * - Domain Events: Significant occurrences within the domain
 * - Repositories: Interfaces for data access (implementations are in the infrastructure layer)
 */
package pt.bayonne.sensei.futmanager.domain;