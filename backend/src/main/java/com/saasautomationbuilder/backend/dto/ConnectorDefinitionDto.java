package com.saasautomationbuilder.backend.dto;

import com.saasautomationbuilder.backend.domain.ConnectorDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorDefinitionDto {
    private Long id;
    private String key;
    private String name;
    private String description;
    private String logoUrl;
    private ConnectorDefinition.AuthenticationType authType;
    private String credentialFieldsSchema; // JSON schema string
    // OAuth specific fields (nullable)
    private String oauth2ClientId;
    private String oauth2Scopes;
    private String oauth2AuthorizationUrl;
    private String oauth2TokenUrl;
    // Timestamps
    private Instant createdAt;
    private Instant updatedAt;
} 