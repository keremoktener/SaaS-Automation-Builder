package com.saasautomationbuilder.backend.repository;

import com.saasautomationbuilder.backend.domain.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {

    // Find connections for a specific user
    List<UserConnection> findByUserId(Long userId);

    // Optional: Find connections for a specific user and connector type
    // List<UserConnection> findByUserIdAndConnectorDefinitionKey(Long userId, String connectorKey);
} 