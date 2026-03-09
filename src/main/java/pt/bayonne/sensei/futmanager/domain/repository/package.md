# Domain Repository Package

Domain repository package containing repository interfaces.

This package contains repository interfaces that define the contracts for data access
operations on domain entities and aggregates. Following the Hexagonal Architecture and
DDD principles, these interfaces are defined in the domain layer, but their implementations
are in the infrastructure layer.

Repository interfaces:
- Define methods for **CRUD operations** on domain entities and aggregates
- Are **technology-agnostic** (no JPA, JDBC, or other technology-specific annotations)
- Represent a **collection of domain objects** in memory
- **Hide the details** of data access and persistence

Examples of repository interfaces in a football manager application might include:
- **PlayerRepository**: For accessing and persisting Player entities
- **TeamRepository**: For accessing and persisting Team entities
- **MatchRepository**: For accessing and persisting Match entities