package com.saasautomationbuilder.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

// Fields are optional for partial updates
@Data
public class UpdateWorkflowRequestDto {

    @Size(max = 255, message = "Workflow name cannot exceed 255 characters")
    private String name; // Can be null if not updating name

    @Size(max = 1024, message = "Description cannot exceed 1024 characters")
    private String description; // Can be null

    private Boolean enabled; // Can be null

    // Assuming trigger/action config are strings (e.g., JSON)
    private String triggerConfig; // Can be null

    private String actionConfig; // Can be null
} 