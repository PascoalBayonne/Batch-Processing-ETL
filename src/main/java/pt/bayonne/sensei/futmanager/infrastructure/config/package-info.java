/**
 * Configuration package containing application configurations.
 * 
 * This package contains configuration classes that set up the application's infrastructure,
 * such as database configurations, messaging configurations, caching configurations, and
 * other technical concerns.
 * 
 * Configuration classes:
 * - Use Spring's @Configuration annotation
 * - Define beans using @Bean methods
 * - Configure technical aspects of the application
 * - Are specific to the infrastructure layer
 * 
 * Examples of configuration classes in a football manager application might include:
 * - DatabaseConfig: Configures the database connection and JPA settings
 * - CacheConfig: Configures caching for the application
 * - AsyncConfig: Configures asynchronous processing
 * - WebConfig: Configures web-related settings (CORS, content negotiation, etc.)
 * - MessagingConfig: Configures messaging (Spring Cloud Stream, Kafka, etc.)
 */
package pt.bayonne.sensei.futmanager.infrastructure.config;