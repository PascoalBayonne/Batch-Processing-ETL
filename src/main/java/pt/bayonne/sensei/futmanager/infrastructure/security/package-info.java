/**
 * Security package containing security-related configurations and implementations.
 * 
 * This package contains security-related configurations and implementations, such as
 * authentication, authorization, and other security concerns. It uses Spring Security
 * to secure the application.
 * 
 * Security classes:
 * - Configure authentication and authorization
 * - Define security filters and handlers
 * - Implement user details services and authentication providers
 * - Configure password encoding and security settings
 * 
 * Examples of security classes in a football manager application might include:
 * - SecurityConfig: Configures Spring Security settings
 * - JwtTokenProvider: Provides JWT token generation and validation
 * - UserDetailsServiceImpl: Implements Spring Security's UserDetailsService
 * - AuthenticationController: Handles authentication requests
 * - CustomAccessDeniedHandler: Handles access denied exceptions
 */
package pt.bayonne.sensei.futmanager.infrastructure.security;