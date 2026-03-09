/**
 * External adapter package containing external service adapters.
 * 
 * This package contains external service adapters that handle communication with
 * external services or APIs. External service adapters implement interfaces defined
 * in the application layer and are responsible for translating between domain objects
 * and external service data formats.
 * 
 * External service adapters:
 * - Implement interfaces defined in the application layer
 * - Use Spring WebClient, RestTemplate, or other HTTP clients
 * - Translate between domain objects and external service data formats
 * - Handle external service-specific concerns (e.g., authentication, error handling)
 * - Implement resilience patterns (e.g., circuit breaker, retry, timeout)
 * 
 * Examples of external service adapters in a football manager application might include:
 * - FootballDataApiAdapter: Interacts with a football data API
 * - WeatherApiAdapter: Interacts with a weather API for match conditions
 * - StatisticsApiAdapter: Interacts with a statistics API for player and team statistics
 */
package pt.bayonne.sensei.futmanager.infrastructure.adapter.external;