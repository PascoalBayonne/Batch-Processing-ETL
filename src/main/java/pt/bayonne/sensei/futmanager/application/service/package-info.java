/**
 * Application service package containing application services.
 * 
 * This package contains application services that orchestrate the flow of data and
 * coordinate domain operations. Application services implement the use cases of the
 * application and act as a facade for the domain layer.
 * 
 * Application services:
 * - Implement input ports (interfaces) defined in the application layer
 * - Use output ports (interfaces) to interact with the infrastructure layer
 * - Coordinate domain operations and manage transactions
 * - Translate between DTOs and domain objects
 * - Don't contain business logic (that belongs in the domain layer)
 * 
 * Examples of application services in a football manager application might include:
 * - PlayerApplicationService: Handles use cases related to players
 * - TeamApplicationService: Handles use cases related to teams
 * - MatchApplicationService: Handles use cases related to matches
 */
package pt.bayonne.sensei.futmanager.application.service;