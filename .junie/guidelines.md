You are an expert in Java programming, Spring Boot, Spring Framework, Maven, JUnit, Spring Modulith, Spring Cloud Stream and related Java technologies.


Code Style and Structure
- Always start anything with Tests. Use TDD techniques before changing or adding a feature. Always, test first.
- Write clean, efficient, and well-documented Java code with accurate Spring Boot examples.
- Use descriptive method and variable names following camelCase convention.
- Structure Spring Boot applications: In terms of design and architecture, use Hexagonal Architecture (Ports and Adapters), Domain-Driven Design and Clean Archiecture. Internal layers can consist in controllers, services, repositories, models, configurations. Separete them in different packages and add always package-info and documentation.

Spring Boot Specifics
- Use Spring Boot starters for quick project setup and dependency management.
- Implement proper use of annotations (e.g., @SpringBootApplication, @RestController, @Service).
- Utilize Spring Boot's auto-configuration features effectively.
- Implement proper exception handling using @ControllerAdvice and @ExceptionHandler.

Naming Conventions
- Use PascalCase for class names (e.g., UserController, OrderService).
- Use camelCase for method and variable names (e.g., findUserById, isOrderValid).
- Use ALL_CAPS for constants (e.g., MAX_RETRY_ATTEMPTS, DEFAULT_PAGE_SIZE).

Java and Spring Boot Usage
- Use Java 21 or later features when applicable (e.g., records, sealed classes, pattern matching).
- Leverage Spring Boot 3.x features and best practices.
- Use Spring Boot best practices and conventions throughout your code.
- Use Spring Data JPA for database operations when applicable. Think always in term of performance by chosing between JPA and Spring Data Jdbc (new jdbc client).
- Implement proper validation using Bean Validation (e.g., @Valid, custom validators).
- Use spring.threads.virtual.enabled=true if necessary

Configuration and Properties
- Use application.properties or application.yml for configuration.
- Implement environment-specific configurations using Spring Profiles.
- Use @ConfigurationProperties for type-safe configuration properties.

Dependency Injection and IoC
- Use constructor injection over field injection for better testability.
- Leverage Spring's IoC container for managing bean lifecycles.


Testing

As we always use DDD and Hexagonal Architecture (clean architecture) the tests should be:
- For Domain layer (using DDD concept, valueObjects, Entities and Aggregates) always test it with pure java junit, enforcing that unit test should not talk with network. It should always be in isolation which means it can never depend on any other unit test. Avoid the spring @Order in test for example. Use Java DataFaker in order to provide some fake and dynamic data in unit tests.
-  Application Layer (Service Layer): use Junit and Mockito
-  Web Adapters (Web Layer): Use Spring RestDocs, Wiremock (for third-party api) and test containers in order to simulate end-to-end test. You can think about MicRocks for mocking the web layer as alternative to Spring RestDocs.
-  Messaging (Adapters): use spring cloud contract (if there is a messaging contract provided by third party), use test containers with kafka, zookeeper and schema registry. If possible write also unit test by using Spring Cloud Stream Test Binder.
- Proxy or third-party web api (internal projects): use Spring Cloud Contract if the API Provider provides it. If there is not contract please use Wiremock in order to mock the third party api.
- If the application layer is connecting to a network resource (e.g., database, external API), use integration tests with Testcontainers and simulate network issues to ensure resiliency, for that use toxy proxy. For example to simulate network issues, you can use Toxiproxy to introduce latency, connection errors, or other network disruptions in your integration tests.


Performance and Scalability
- Implement caching strategies using Spring Cache abstraction.
- Use async processing with @Async for non-blocking operations.
- Configuring threads for Spring MVC and Database centric app
- Implement proper database indexing and query optimization.

Resiliency patterns:
- Follow resiliency patterns such as:
  - Circuit Breaker
  - Timeouts
  - Fallback
  - Retries

Security
- Implement Spring Security for authentication and authorization.
- Use proper password encoding (e.g., BCrypt).
- Implement CORS configuration when necessary.

Logging and Monitoring
- Use SLF4J with Logback for logging (lombok can offer it).
- Implement proper log levels (ERROR, WARN, INFO, DEBUG).
- Use Spring Boot Actuator for application monitoring and metrics.

API Documentation
- Use Spring Rest Docs for documenting REST APIs.
- Use Springdoc OpenAPI (formerly Swagger) for API documentation.
- Ensure API documentation is up-to-date with code changes.
- Try to document application by using C4 model (Context, Container, Component and Code) and use plantuml for it.

Data Access and ORM
- Use Spring Data JPA for database operations.
- Implement proper entity relationships and cascading.
- Use database migrations with tools like Liquibase.

Build and Deployment
- Use Maven for dependency management and build processes.
- Implement proper profiles for different environments (dev, test, prod).
- Use Docker for containerization if applicable.

Follow best practices for:
- RESTful API design (proper use of HTTP methods, status codes, etc.). Becareful of anemic REST API when dealing with a DDD project.
- Microservices architecture (if applicable).
- Asynchronous processing using Spring's @Async or reactive programming with Spring WebFlux. Think what is better between Spring WebFlux or the new java 21 virtual thread.
- Use principles such as Tell Don't Ask, Law of Demeter, and Single Responsibility Principle.


Adhere to SOLID principles and maintain high cohesion and low coupling in your Spring Boot application design.
 - Direct communication with customers and third-party products technical support