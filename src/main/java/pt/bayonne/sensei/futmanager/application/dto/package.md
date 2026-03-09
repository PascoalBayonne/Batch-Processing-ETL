# Data Transfer Object (DTO) Package

Data Transfer Object (DTO) package containing DTOs.

This package contains DTOs that are used to transfer data between the application layer
and the infrastructure layer. DTOs are simple objects that contain only data and no behavior.
They are used to decouple the domain model from the external interfaces.

DTOs:
- Are **simple objects with getters and setters** (or records in Java 21)
- Contain **only data and no behavior**
- Are used to **transfer data across layer boundaries**
- Are **tailored to specific use cases or views**
- May include **validation annotations** for input validation

Examples of DTOs in a football manager application might include:
- **PlayerDTO**: Contains player data for display or input
- **TeamDTO**: Contains team data for display or input
- **MatchDTO**: Contains match data for display or input