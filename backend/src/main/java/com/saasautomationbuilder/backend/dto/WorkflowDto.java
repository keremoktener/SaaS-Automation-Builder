package com.saasautomationbuilder.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDto {
    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private String triggerConfig; // Keep as String for now, could be JSON object later
    private String actionConfig;  // Keep as String for now, could be JSON object later
    private Long userId; // Include owner ID
    private Instant createdAt;
    private Instant updatedAt;
} 