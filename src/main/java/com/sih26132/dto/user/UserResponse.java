package com.sih26132.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;
    private String fullName;
    private String phone;
    private String email;
    private String role;
    private String preferredLanguage;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}