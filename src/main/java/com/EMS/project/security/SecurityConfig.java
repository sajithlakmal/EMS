package com.EMS.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the Employee Management System.
 * Configures HTTP Basic authentication and role-based access control.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception in case of any configuration errors
     */
    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery)
                .csrf(csrf -> csrf.disable())
                // Authorize HTTP requests
                .authorizeHttpRequests(auth -> auth
                        // Allow GET requests to /api/** for users with ROLE_ADMIN or ROLE_USER
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER")
                        // Allow other HTTP methods to /api/** only for users with ROLE_ADMIN
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        // Require authentication for any other requests
                        .anyRequest().authenticated()
                )
                // Configure HTTP Basic authentication
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Configures in-memory authentication with two users: empadmin (ROLE_ADMIN) and emp001 (ROLE_USER).
     *
     * @return the UserDetailsService containing the in-memory users
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Define the admin user
        UserDetails adminUser = User.withDefaultPasswordEncoder()
                .username("empadmin")
                .password("exam#123")
                .roles("ADMIN")
                .build();

        // Define the standard user
        UserDetails standardUser = User.withDefaultPasswordEncoder()
                .username("emp001")
                .password("emppw#123")
                .roles("USER")
                .build();

        // Return an in-memory user details manager
        return new InMemoryUserDetailsManager(adminUser, standardUser);
    }
}
