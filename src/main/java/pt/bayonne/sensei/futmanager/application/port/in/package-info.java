/**
 * Input port package containing input ports (interfaces).
 * 
 * This package contains input ports that define the contract for the application layer
 * to be used by the infrastructure layer (specifically, the web adapters). Input ports
 * represent the use cases of the application and are implemented by application services.
 * 
 * Input ports:
 * - Are interfaces that define the contract for the application layer
 * - Represent the use cases of the application
 * - Are implemented by application services
 * - Are used by controllers in the infrastructure layer
 * - Accept and return DTOs, not domain objects
 * 
 * Examples of input ports in a football manager application might include:
 * - PlayerUseCase: Defines use cases related to players
 * - TeamUseCase: Defines use cases related to teams
 * - MatchUseCase: Defines use cases related to matches
 */
package pt.bayonne.sensei.futmanager.application.port.in;