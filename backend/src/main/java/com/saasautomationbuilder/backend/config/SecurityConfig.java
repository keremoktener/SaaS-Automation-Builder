package com.saasautomationbuilder.backend.config;

import com.saasautomationbuilder.backend.security.FirebaseTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // For CSRF disabling
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Optional import if using WebSecurityCustomizer
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable method-level security (e.g., @PreAuthorize)
@RequiredArgsConstructor
public class SecurityConfig {

    private final FirebaseTokenFilter firebaseTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (Cross-Site Request Forgery) as we use token-based auth (stateless)
            .csrf(AbstractHttpConfigurer::disable)

            // Enable CORS and use the default configuration defined elsewhere (e.g., WebConfig)
            .cors(cors -> {})

            // Set session management to stateless - essential for REST APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configure authorization rules for HTTP requests
            .authorizeHttpRequests(authz -> authz
                // Define public endpoints here if needed (e.g., actuator health)
                // .requestMatchers("/actuator/health").permitAll()

                // Secure all other API endpoints under /api/**
                .requestMatchers("/api/**").authenticated()

                // Deny any other request that doesn't match the rules above
                .anyRequest().denyAll() // Or .anyRequest().authenticated() if default is secure
            )

            // Add our custom Firebase token filter before the standard username/password filter
            .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Optional Bean: Customize WebSecurity to ignore certain paths (e.g., static assets)
    // This bypasses the Spring Security filter chain completely for these paths.
    /*
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
    */
}
