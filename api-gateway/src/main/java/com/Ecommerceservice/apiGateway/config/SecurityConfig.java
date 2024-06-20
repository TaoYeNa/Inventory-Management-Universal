package com.Ecommerceservice.apiGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity

public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable) // Using the Consumer functional interface to disable CSRF
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/eureka/**").permitAll() // Permit all requests to "/eureka/**"
                        .anyExchange().authenticated() // All other requests must be authenticated
                ).oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults())).build();
    }
}
