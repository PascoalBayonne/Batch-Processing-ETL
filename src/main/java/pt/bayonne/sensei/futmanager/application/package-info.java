/**
 * Application layer package following Hexagonal Architecture principles.
 * 
 * This package contains the application services, use cases, and ports (interfaces)
 * that orchestrate the flow of data to and from the domain layer. The application layer
 * acts as a mediator between the domain layer and the infrastructure layer.
 * 
 * The application layer is organized into:
 * - Application Services: Orchestrate the flow of data and coordinate domain operations
 * - DTOs (Data Transfer Objects): Objects used to transfer data between layers
 * - Ports: Interfaces that define the contracts for the infrastructure layer to implement
 *   - Input Ports: Interfaces implemented by application services (used by controllers)
 *   - Output Ports: Interfaces implemented by adapters (used by application services)
 * - Use Cases: Specific business operations or user interactions
 * 
 * This layer depends on the domain layer but is independent of the infrastructure layer.
 */
package pt.bayonne.sensei.futmanager.application;