# Infrastructure Layer

Infrastructure layer package following Hexagonal Architecture principles.

This package contains the adapters that implement the ports defined in the application layer.
The infrastructure layer is responsible for the technical concerns of the application,
such as persistence, web interfaces, messaging, and external services integration.

The infrastructure layer is organized into:
- **Adapters**: Implementations of the ports defined in the application layer
  - **Web Adapters**: Controllers, REST APIs, and web-related configurations
  - **Persistence Adapters**: Repository implementations, database configurations
  - **Messaging Adapters**: Message producers and consumers
  - **External Service Adapters**: Clients for external services
- **Configurations**: Technical configurations for the application
- **Security**: Security-related configurations and implementations

This layer depends on both the domain and application layers.