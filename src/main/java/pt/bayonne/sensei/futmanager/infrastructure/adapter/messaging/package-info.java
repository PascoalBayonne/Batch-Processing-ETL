/**
 * Messaging adapter package containing messaging adapters.
 * 
 * This package contains messaging adapters that handle message-based communication with
 * external systems or between different parts of the application. Messaging adapters
 * can be producers (sending messages) or consumers (receiving messages).
 * 
 * Messaging adapters:
 * - Implement messaging-related interfaces defined in the application layer
 * - Use Spring Cloud Stream or other messaging frameworks
 * - Translate between domain objects and messages
 * - Handle messaging-specific concerns (e.g., serialization, error handling)
 * 
 * Examples of messaging adapters in a football manager application might include:
 * - MatchEventProducer: Produces events related to matches
 * - PlayerEventConsumer: Consumes events related to players
 * - TeamEventProducer: Produces events related to teams
 */
package pt.bayonne.sensei.futmanager.infrastructure.adapter.messaging;