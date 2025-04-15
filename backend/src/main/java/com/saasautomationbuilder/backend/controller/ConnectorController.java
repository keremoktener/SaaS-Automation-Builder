package com.saasautomationbuilder.backend.controller;

import com.saasautomationbuilder.backend.domain.User;
import com.saasautomationbuilder.backend.dto.ConnectorDefinitionDto;
import com.saasautomationbuilder.backend.dto.CreateUserConnectionRequestDto;
import com.saasautomationbuilder.backend.dto.UserConnectionDto;
import com.saasautomationbuilder.backend.service.ConnectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") // Base path
@RequiredArgsConstructor
public class ConnectorController {

    private final ConnectorService connectorService;

    // --- Connector Definitions ---

    @GetMapping("/connector-definitions")
    public ResponseEntity<List<ConnectorDefinitionDto>> getAvailableConnectors() {
        List<ConnectorDefinitionDto> connectors = connectorService.getAvailableConnectors();
        return ResponseEntity.ok(connectors);
    }

    @GetMapping("/connector-definitions/{key}")
    public ResponseEntity<ConnectorDefinitionDto> getConnectorDefinitionByKey(@PathVariable String key) {
        ConnectorDefinitionDto definition = connectorService.getConnectorDefinitionByKey(key);
        return ResponseEntity.ok(definition);
    }
    
    // TODO: Add admin endpoint to create definitions if needed

    // --- User Connections ---

    @GetMapping("/user-connections")
    public ResponseEntity<List<UserConnectionDto>> getUserConnections(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<UserConnectionDto> connections = connectorService.getUserConnections(currentUser);
        return ResponseEntity.ok(connections);
    }

    @PostMapping("/user-connections")
    public ResponseEntity<UserConnectionDto> createUserConnection(
            @Valid @RequestBody CreateUserConnectionRequestDto requestDto,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        UserConnectionDto createdConnection = connectorService.createUserConnection(requestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConnection);
    }
    
    @DeleteMapping("/user-connections/{connectionId}")
    public ResponseEntity<Void> deleteUserConnection(
            @PathVariable Long connectionId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        connectorService.deleteUserConnection(connectionId, currentUser);
        return ResponseEntity.noContent().build();
    }
} 