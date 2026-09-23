package com.sih26132.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        System.out.println(
                "JWT FILTER - Authorization header present: "
                        + (authHeader != null)
        );

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println(
                    "JWT FILTER - No Bearer token found"
            );

            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken =
                authHeader.substring(7);

        final String username;

        try {

            username = jwtService.extractUsername(jwtToken);

            System.out.println(
                    "JWT FILTER - Username extracted: "
                            + username
            );

        } catch (Exception exception) {

            System.out.println(
                    "JWT FILTER - JWT extraction FAILED: "
                            + exception.getClass().getName()
                            + " - "
                            + exception.getMessage()
            );

            filterChain.doFilter(request, response);
            return;
        }

        if (username != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails;

            try {

                userDetails =
                        userDetailsService
                                .loadUserByUsername(username);

                System.out.println(
                        "JWT FILTER - User loaded: "
                                + userDetails.getUsername()
                );

                System.out.println(
                        "JWT FILTER - Authorities: "
                                + userDetails.getAuthorities()
                );

            } catch (Exception exception) {

                System.out.println(
                        "JWT FILTER - User loading FAILED: "
                                + exception.getClass().getName()
                                + " - "
                                + exception.getMessage()
                );

                filterChain.doFilter(request, response);
                return;
            }

            try {

                boolean valid =
                        jwtService.isTokenValid(
                                jwtToken,
                                userDetails
                        );

                System.out.println(
                        "JWT FILTER - Token valid: "
                                + valid
                );

                if (valid) {

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "JWT FILTER - Authentication SUCCESS"
                    );
                }

            } catch (Exception exception) {

                System.out.println(
                        "JWT FILTER - Token validation FAILED: "
                                + exception.getClass().getName()
                                + " - "
                                + exception.getMessage()
                );

                SecurityContextHolder
                        .clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}