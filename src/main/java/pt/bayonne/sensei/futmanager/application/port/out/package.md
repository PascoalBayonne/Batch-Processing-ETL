# Output Port Package

Output port package containing output ports (interfaces).

This package contains output ports that define the contract for the infrastructure layer
to be used by the application layer. Output ports represent the dependencies of the
application layer on external systems and are implemented by adapters in the infrastructure layer.

Output ports:
- Are **interfaces that define the contract** for the infrastructure layer
- Represent the **dependencies of the application layer** on external systems
- Are **implemented by adapters** in the infrastructure layer
- Are **used by application services**
- Accept and return **domain objects, not DTOs**

Examples of output ports in a football manager application might include:
- **PlayerRepository**: Defines methods for accessing and persisting Player entities
- **TeamRepository**: Defines methods for accessing and persisting Team entities
- **MatchRepository**: Defines methods for accessing and persisting Match entities
- **ExternalApiPort**: Defines methods for interacting with external APIs