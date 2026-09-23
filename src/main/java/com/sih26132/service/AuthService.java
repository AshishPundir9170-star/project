package com.sih26132.service;

import com.sih26132.dto.auth.AuthResponse;
import com.sih26132.dto.auth.LoginRequest;
import com.sih26132.dto.auth.RegisterRequest;
import com.sih26132.entity.Role;
import com.sih26132.entity.User;
import com.sih26132.repository.RoleRepository;
import com.sih26132.repository.UserRepository;
import com.sih26132.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

        if (request.getEmail() != null
                && userRepository.existsByEmail(request.getEmail())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered"
            );
        }

        if (request.getPhone() != null
                && userRepository.existsByPhone(request.getPhone())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Phone number already registered"
            );
        }

        Role role = roleRepository.findByCode("FARMER")
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "FARMER role not found"
                        )
                );

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(role);

        user.setPreferredLanguage(
                request.getPreferredLanguage() != null
                        ? request.getPreferredLanguage()
                        : "en"
        );

        user.setActive(true);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                "Bearer",
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getRole().getCode(),
                savedUser.getPreferredLanguage()
        );
    }

    public AuthResponse login(LoginRequest request) {

        String identifier = request.getIdentifier();

        User user = userRepository
                .findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email/phone or password"
                        )
                );

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword()
                    )
            );

        } catch (Exception exception) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email/phone or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getFullName(),
                user.getRole().getCode(),
                user.getPreferredLanguage()
        );
    }
}