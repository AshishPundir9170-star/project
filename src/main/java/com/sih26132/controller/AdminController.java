package com.sih26132.controller;

import com.sih26132.dto.admin.ChangeRoleRequest;
import com.sih26132.entity.Role;
import com.sih26132.entity.User;
import com.sih26132.repository.RoleRepository;
import com.sih26132.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminController(
            UserRepository userRepository,
            RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<String> changeUserRole(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeRoleRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Role role = roleRepository.findByCode(request.getRoleCode())
                .orElseThrow(() ->
                        new RuntimeException("Role not found"));

        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok(
                "User role changed to " + role.getCode()
        );
    }
}