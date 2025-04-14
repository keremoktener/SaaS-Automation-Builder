package com.saasautomationbuilder.backend.dto;

import lombok.Data;

// Using jakarta validation for basic checks
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CreateWorkflowRequestDto {

    @NotBlank(message = "Workflow name cannot be blank")
    @Size(max = 255, message = "Workflow name cannot exceed 255 characters")
    private String name;

    @Size(max = 1024, message = "Description cannot exceed 1024 characters")
    private String description;

    @NotNull(message = "Enabled status must be provided")
    private Boolean enabled = false; // Default to false

    @NotBlank(message = "Trigger configuration cannot be blank")
    private String triggerConfig; // Accept as String/JSON for now

    @NotBlank(message = "Action configuration cannot be blank")
    private String actionConfig; // Accept as String/JSON for now

    // We'll add userId here later when auth is implemented,
    // or infer it from the authenticated user context in the service/controller.
    // For now, we might need to pass it explicitly or default it.
    // @NotNull(message = "User ID must be provided")
    // private Long userId;
} 