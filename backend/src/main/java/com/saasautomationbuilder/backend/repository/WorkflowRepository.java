package com.saasautomationbuilder.backend.repository;

import com.saasautomationbuilder.backend.domain.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    // Find workflows by user ID
    List<Workflow> findByUserId(Long userId);

    // Find workflows by user's Firebase UID (might require a join or separate query)
    // List<Workflow> findByUserFirebaseUid(String firebaseUid);
} 