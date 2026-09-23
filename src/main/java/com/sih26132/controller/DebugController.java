package com.sih26132.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/api/debug/auth")
    public Map<String, Object> authentication(Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "authenticated",
                authentication != null && authentication.isAuthenticated()
        );

        response.put(
                "name",
                authentication != null
                        ? authentication.getName()
                        : null
        );

        response.put(
                "authorities",
                authentication != null
                        ? authentication.getAuthorities()
                        : null
        );

        response.put(
                "principal",
                authentication != null
                        ? authentication.getPrincipal().getClass().getName()
                        : null
        );

        return response;
    }
}