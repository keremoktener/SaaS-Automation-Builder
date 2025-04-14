package com.saasautomationbuilder.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "workflows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1024) // Example length
    private String description;

    @Column(nullable = false)
    private boolean enabled = false; // Default to disabled

    @Lob // Use Lob for potentially large text fields (e.g., JSON)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String triggerConfig; // Store trigger configuration (e.g., as JSON)

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String actionConfig; // Store action configuration (e.g., as JSON)

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch user unless needed
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
} 