package com.saasautomationbuilder.backend.repository;

import com.saasautomationbuilder.backend.domain.ConnectorDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectorDefinitionRepository extends JpaRepository<ConnectorDefinition, Long> {
    Optional<ConnectorDefinition> findByKey(String key);
} 