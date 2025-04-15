package com.saasautomationbuilder.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "connector_definitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key; // Unique key, e.g., "slack", "google_sheets"

    @Column(nullable = false)
    private String name; // User-friendly name, e.g., "Slack", "Google Sheets"

    @Column(length = 1024)
    private String description;

    @Column
    private String logoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthenticationType authType; // e.g., API_KEY, OAUTH2

    @Lob // Use Lob for potentially large text fields (e.g., JSON)
    @Column(columnDefinition = "TEXT")
    private String credentialFieldsSchema; // Describes fields needed (e.g., JSON schema for API keys)

    // -- Fields relevant for OAuth2 --
    @Column
    private String oauth2ClientId; // Client ID for OAuth2 flow

    @Column
    private String oauth2Scopes; // Comma-separated scopes needed

    @Column
    private String oauth2AuthorizationUrl; // URL to redirect user for OAuth2

    @Column
    private String oauth2TokenUrl; // URL to exchange code for token
    // Note: Client Secret should NOT be stored here. Use application properties/secrets manager.
    // ----------------------------------

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    // Enum for Authentication Types
    public enum AuthenticationType {
        API_KEY, OAUTH2
        // Add other types like BASIC_AUTH if needed
    }
} 