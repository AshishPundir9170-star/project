package com.sih26132.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================================================
    // AUTHENTICATION MANAGER
    // =========================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }


    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();


        // -----------------------------------------------------
        // ALLOWED FRONTEND ORIGINS
        // -----------------------------------------------------

        configuration.setAllowedOrigins(
                Arrays.asList(

                        // Local frontend
                        "http://localhost:5500",

                        // Local frontend - 127.0.0.1
                        "http://127.0.0.1:5500",

                        // GitHub Pages frontend
                        "https://ashishpundir9170-star.github.io"
                )
        );


        // -----------------------------------------------------
        // ALLOWED HTTP METHODS
        // -----------------------------------------------------

        configuration.setAllowedMethods(
                Arrays.asList(

                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );


        // -----------------------------------------------------
        // ALLOWED HEADERS
        // -----------------------------------------------------

        configuration.setAllowedHeaders(
                Arrays.asList(

                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "Origin",
                        "X-Requested-With"
                )
        );


        // -----------------------------------------------------
        // EXPOSED HEADERS
        // -----------------------------------------------------

        configuration.setExposedHeaders(
                Arrays.asList(
                        "Authorization"
                )
        );


        // -----------------------------------------------------
        // CREDENTIALS
        // -----------------------------------------------------

        configuration.setAllowCredentials(true);


        // -----------------------------------------------------
        // REGISTER CORS CONFIGURATION
        // -----------------------------------------------------

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }


    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {


        http

                // -------------------------------------------------
                // CSRF
                // -------------------------------------------------

                .csrf(csrf -> csrf.disable())


                // -------------------------------------------------
                // CORS
                // -------------------------------------------------

                .cors(Customizer.withDefaults())


                // -------------------------------------------------
                // SESSION
                // -------------------------------------------------

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // -------------------------------------------------
                // AUTHORIZATION
                // -------------------------------------------------

                .authorizeHttpRequests(auth -> auth


                        // =========================================
                        // AUTH APIs
                        // =========================================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()


                        // =========================================
                        // HEALTH CHECK
                        // =========================================

                        .requestMatchers(
                                "/actuator/health"
                        ).permitAll()


                        // =========================================
                        // SWAGGER
                        // =========================================

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()


                        // =========================================
                        // ML APIs
                        // =========================================

                        .requestMatchers(
                                "/api/ml/**"
                        ).permitAll()


                        // =========================================
                        // EVERYTHING ELSE
                        // =========================================

                        .anyRequest().authenticated()
                )


                // -------------------------------------------------
                // EXCEPTION HANDLING
                // -------------------------------------------------

                .exceptionHandling(exception -> exception


                        // 401
                        .authenticationEntryPoint(
                                authenticationEntryPoint()
                        )


                        // 403
                        .accessDeniedHandler(
                                accessDeniedHandler()
                        )
                )


                // -------------------------------------------------
                // JWT FILTER
                // -------------------------------------------------

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }


    // =========================================================
    // 401 UNAUTHORIZED
    // =========================================================

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, authException) -> {

            response.setStatus(
                    HttpServletResponseStatus.UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}"
            );
        };
    }


    // =========================================================
    // 403 FORBIDDEN
    // =========================================================

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return (request, response, accessDeniedException) -> {

            response.setStatus(
                    HttpServletResponseStatus.FORBIDDEN
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{\"error\":\"Forbidden\",\"message\":\"Access denied\"}"
            );
        };
    }


    // =========================================================
    // HTTP STATUS CONSTANTS
    // =========================================================

    private static class HttpServletResponseStatus {

        private static final int UNAUTHORIZED = 401;

        private static final int FORBIDDEN = 403;
    }
}