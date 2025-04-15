package com.saasautomationbuilder.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

// DTO for representing a user's connection to a service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionDto {
    private Long id;
    private Long userId;
    private String connectorKey; // Key of the connected service (e.g., "slack")
    private String connectorName; // Display name (e.g., "Slack")
    private String connectorLogoUrl; // Logo URL
    private String connectionName; // User-defined name for this connection
    private Instant expiresAt; // Nullable
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    // Note: Credentials are intentionally omitted for security
} 