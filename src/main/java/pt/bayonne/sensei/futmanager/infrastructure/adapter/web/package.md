# Web Adapter Package

Web adapter package containing web adapters (controllers).

This package contains web adapters that implement the REST API of the application.
Web adapters use input ports (interfaces) defined in the application layer to interact
with the application layer. They are responsible for handling HTTP requests and responses,
input validation, and error handling.

Web adapters:
- **Use input ports** (interfaces) defined in the application layer
- **Handle HTTP requests and responses**
- **Perform input validation**
- **Handle errors and exceptions**
- **Translate between DTOs** and HTTP requests/responses

Examples of web adapters in a football manager application might include:
- **PlayerController**: Handles HTTP requests related to players
- **TeamController**: Handles HTTP requests related to teams
- **MatchController**: Handles HTTP requests related to matches