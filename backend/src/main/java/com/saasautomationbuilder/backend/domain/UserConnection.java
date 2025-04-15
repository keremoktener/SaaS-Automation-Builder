package com.saasautomationbuilder.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user_connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER) // Eager fetch definition details
    @JoinColumn(name = "connector_definition_id", nullable = false)
    private ConnectorDefinition connectorDefinition;

    @Column(nullable = false)
    private String connectionName; // User-defined name for this specific connection

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String encryptedCredentials; // Placeholder for securely stored credentials
    // IMPORTANT: This field MUST store encrypted data. We will need a proper encryption strategy.
    
    @Column
    private Instant expiresAt; // Optional: For credentials that expire (like OAuth tokens)

    @Column(nullable = false)
    private boolean active = true; // Is the connection currently valid/active?

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
} 