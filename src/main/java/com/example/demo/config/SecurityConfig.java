package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String url;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        logger.info("🔐 Security Filter Chain initialized");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> {
                    logger.info("🔎 Configuring authorization rules");

                    // 1. Browser/System Rules
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    auth.requestMatchers("/api/payment/**").permitAll();

                    // 2. Public Auth & Onboarding
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers("/api/onboarding/approve/**", "/api/onboarding/reject/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/onboarding/apply").permitAll();
                    // 3. SECURE - Specific POST/Write Actions (Must be above GET permitAll)
                    auth.requestMatchers(HttpMethod.POST, "/api/products/add").authenticated();;
                    auth.requestMatchers(HttpMethod.POST, "/api/rentals/**").permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/api/onboarding/apply").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/products/*/reviews").permitAll();
                    auth.requestMatchers("/api/orders/Booking").permitAll();
                    // 4. PUBLIC - General Viewing (GET requests)
                    auth.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/rentals/**").permitAll();
                    // 5. CATCH-ALL (Must be at the very bottom)
                    auth.anyRequest().authenticated();

                    logger.info("🌍 Security rules configured");
                })

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    // ====================== JWT DECODER (Keycloak at 8080) ======================
    @Bean
    public JwtDecoder jwtDecoder() {
        logger.info("🔑 Creating JwtDecoder for Keycloak");

        RestTemplate restTemplate = new RestTemplate();
        var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(15000);
        restTemplate.setRequestFactory(factory);

        // Ensure this matches your Keycloak Port (default 8080)
        return NimbusJwtDecoder.withJwkSetUri(url)
                .restOperations(restTemplate)
                .build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow your Next.js frontend ports
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:3001", "http://localhost:3002"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}