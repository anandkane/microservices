package org.example.explore.virtualstore.productservice.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // formatter:off
        http.authorizeRequests(authorizeRequests -> authorizeRequests
                   .requestMatchers("/products/**").permitAll()
                   .requestMatchers("/error").permitAll()
                   .requestMatchers("/h2-console/**").permitAll()
                   .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/products/**", "/h2-console/**"))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        http.oauth2ResourceServer(server -> server.jwt(withDefaults()));
        // formatter:on
        return http.build();
    }
}
