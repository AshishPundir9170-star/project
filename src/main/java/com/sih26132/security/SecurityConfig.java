package com.sih26132.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
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
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        /*
         * IMPORTANT:
         * Do NOT use "*"
         * when allowCredentials(true) is enabled.
         */

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "https://ashishpundir9170-star.github.io"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS",
                "HEAD"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        configuration.setExposedHeaders(Arrays.asList(
                "Authorization"
        ));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // -------------------------------------------------
            // CORS
            // -------------------------------------------------

            .cors(Customizer.withDefaults())

            // -------------------------------------------------
            // CSRF
            // -------------------------------------------------

            .csrf(csrf -> csrf.disable())

            // -------------------------------------------------
            // STATELESS JWT SESSION
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

                // CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()

                // Authentication APIs
                .requestMatchers(
                    "/api/auth/**"
                )
                .permitAll()

                // Health check
                .requestMatchers(
                    "/actuator/health"
                )
                .permitAll()

                // Swagger
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                )
                .permitAll()

                // ML APIs
                .requestMatchers(
                    "/api/ml/**"
                )
                .permitAll()

                // Everything else requires JWT
                .anyRequest()
                .authenticated()
            )

            // -------------------------------------------------
            // EXCEPTION HANDLING
            // -------------------------------------------------

            .exceptionHandling(exception -> exception

                .authenticationEntryPoint(
                    authenticationEntryPoint()
                )

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

            response.setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

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

            response.setStatus(403);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(
                "{\"error\":\"Forbidden\",\"message\":\"Access denied\"}"
            );
        };
    }
}