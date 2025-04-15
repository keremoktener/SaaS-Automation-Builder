package com.saasautomationbuilder.backend.service;

import com.saasautomationbuilder.backend.domain.ConnectorDefinition;
import com.saasautomationbuilder.backend.domain.User;
import com.saasautomationbuilder.backend.domain.UserConnection;
import com.saasautomationbuilder.backend.dto.ConnectorDefinitionDto;
import com.saasautomationbuilder.backend.dto.CreateUserConnectionRequestDto;
import com.saasautomationbuilder.backend.dto.UserConnectionDto;
import com.saasautomationbuilder.backend.exception.ResourceNotFoundException;
import com.saasautomationbuilder.backend.repository.ConnectorDefinitionRepository;
import com.saasautomationbuilder.backend.repository.UserConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// TODO: Import encryption service later
// import com.saasautomationbuilder.backend.security.EncryptionService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConnectorService {

    private final ConnectorDefinitionRepository connectorDefinitionRepository;
    private final UserConnectionRepository userConnectionRepository;
    // TODO: Inject EncryptionService later
    // private final EncryptionService encryptionService;

    // --- Connector Definitions ---

    @Transactional(readOnly = true)
    public List<ConnectorDefinitionDto> getAvailableConnectors() {
        return connectorDefinitionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConnectorDefinitionDto getConnectorDefinitionByKey(String key) {
        return connectorDefinitionRepository.findByKey(key)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Connector definition not found with key: " + key));
    }
    
    // Admin method (potentially) to add new connector definitions
    // @Transactional
    // public ConnectorDefinitionDto createConnectorDefinition(...) { ... }

    // --- User Connections ---

    @Transactional(readOnly = true)
    public List<UserConnectionDto> getUserConnections(User currentUser) {
        return userConnectionRepository.findByUserId(currentUser.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserConnectionDto createUserConnection(CreateUserConnectionRequestDto requestDto, User currentUser) {
        ConnectorDefinition definition = connectorDefinitionRepository.findByKey(requestDto.getConnectorKey())
                .orElseThrow(() -> new ResourceNotFoundException("Connector definition not found with key: " + requestDto.getConnectorKey()));

        // --- IMPORTANT: Credential Handling Placeholder ---
        // 1. Validate incoming credentials against definition.credentialFieldsSchema
        // 2. Encrypt the credentials using a strong encryption service
        // String encryptedCredentials = encryptionService.encrypt(mapToJson(requestDto.getCredentials()));
        String encryptedCredentials = "PLACEHOLDER_ENCRYPTED_DATA_FOR_" + requestDto.getConnectorKey(); // Replace this!
        // -----------------------------------------------

        UserConnection newConnection = new UserConnection();
        newConnection.setUser(currentUser);
        newConnection.setConnectorDefinition(definition);
        newConnection.setConnectionName(requestDto.getConnectionName());
        newConnection.setEncryptedCredentials(encryptedCredentials);
        // Set expiresAt if applicable (e.g., for OAuth)
        // newConnection.setExpiresAt(...);
        newConnection.setActive(true);

        UserConnection savedConnection = userConnectionRepository.save(newConnection);
        return mapToDto(savedConnection);
    }
    
     @Transactional
    public void deleteUserConnection(Long connectionId, User currentUser) {
        UserConnection connection = userConnectionRepository.findById(connectionId)
            .orElseThrow(() -> new ResourceNotFoundException("User connection not found with id: " + connectionId));
        
        // Check ownership
        if (!connection.getUser().getId().equals(currentUser.getId())) {
            throw new com.saasautomationbuilder.backend.exception.UnauthorizedAccessException("User does not have permission to delete this connection.");
        }
        
        userConnectionRepository.delete(connection);
    }

    // TODO: Add methods for updating connections, refreshing OAuth tokens etc.

    // --- Helper DTO Mappers ---

    private ConnectorDefinitionDto mapToDto(ConnectorDefinition definition) {
        return new ConnectorDefinitionDto(
                definition.getId(),
                definition.getKey(),
                definition.getName(),
                definition.getDescription(),
                definition.getLogoUrl(),
                definition.getAuthType(),
                definition.getCredentialFieldsSchema(),
                // Include OAuth fields only if relevant
                definition.getAuthType() == ConnectorDefinition.AuthenticationType.OAUTH2 ? definition.getOauth2ClientId() : null,
                definition.getAuthType() == ConnectorDefinition.AuthenticationType.OAUTH2 ? definition.getOauth2Scopes() : null,
                definition.getAuthType() == ConnectorDefinition.AuthenticationType.OAUTH2 ? definition.getOauth2AuthorizationUrl() : null,
                definition.getAuthType() == ConnectorDefinition.AuthenticationType.OAUTH2 ? definition.getOauth2TokenUrl() : null,
                definition.getCreatedAt(),
                definition.getUpdatedAt()
        );
    }

    private UserConnectionDto mapToDto(UserConnection connection) {
        // IMPORTANT: NEVER return raw or encrypted credentials in the DTO
        return new UserConnectionDto(
                connection.getId(),
                connection.getUser().getId(),
                connection.getConnectorDefinition().getKey(),
                connection.getConnectorDefinition().getName(), // Include for convenience
                connection.getConnectorDefinition().getLogoUrl(), // Include for convenience
                connection.getConnectionName(),
                connection.getExpiresAt(),
                connection.isActive(),
                connection.getCreatedAt(),
                connection.getUpdatedAt()
        );
    }
    
    // Helper to convert map to JSON string (replace with proper JSON library later)
    // private String mapToJson(Map<String, String> map) { ... }
} 