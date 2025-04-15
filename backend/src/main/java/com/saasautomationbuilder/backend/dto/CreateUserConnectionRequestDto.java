package com.saasautomationbuilder.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class CreateUserConnectionRequestDto {

    @NotBlank(message = "Connector key cannot be blank")
    private String connectorKey; // Key of the connector definition (e.g., "slack")

    @NotBlank(message = "Connection name cannot be blank")
    @Size(max = 255)
    private String connectionName; // User-defined name for the connection

    @NotNull(message = "Credentials map cannot be null")
    // Credentials needed for the connection (e.g., API key, OAuth code/tokens)
    // Using Map<String, String> for flexibility, but could be more specific
    private Map<String, String> credentials; 
} 