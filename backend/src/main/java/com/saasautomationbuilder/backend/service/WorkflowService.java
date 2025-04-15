package com.saasautomationbuilder.backend.service;

import com.saasautomationbuilder.backend.domain.User;
import com.saasautomationbuilder.backend.domain.Workflow;
import com.saasautomationbuilder.backend.dto.CreateWorkflowRequestDto;
import com.saasautomationbuilder.backend.dto.UpdateWorkflowRequestDto;
import com.saasautomationbuilder.backend.dto.WorkflowDto;
import com.saasautomationbuilder.backend.exception.ResourceNotFoundException;
import com.saasautomationbuilder.backend.exception.UnauthorizedAccessException;
import com.saasautomationbuilder.backend.repository.UserRepository;
import com.saasautomationbuilder.backend.repository.WorkflowRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final UserRepository userRepository; // Needed to associate workflow with user

    // Temporary: Get workflows for a specific user ID (replace with authenticated user later)
    @Transactional(readOnly = true)
    public List<WorkflowDto> getWorkflowsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        return workflowRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkflowDto getWorkflowById(Long workflowId, User currentUser) {
        Workflow workflow = findWorkflowByIdOrThrow(workflowId);
        checkOwnership(workflow, currentUser);
        return mapToDto(workflow);
    }

    // Updated: Create workflow for the provided User object
    @Transactional
    public WorkflowDto createWorkflow(CreateWorkflowRequestDto requestDto, User user) {
        Workflow workflow = new Workflow();
        workflow.setUser(user); // Assign owner first
        updateWorkflowEntityFromDto(workflow, requestDto); // Use helper for mapping

        Workflow savedWorkflow = workflowRepository.save(workflow);
        return mapToDto(savedWorkflow);
    }

    @Transactional
    public WorkflowDto updateWorkflow(Long workflowId, UpdateWorkflowRequestDto requestDto, User currentUser) {
        Workflow workflow = findWorkflowByIdOrThrow(workflowId);
        checkOwnership(workflow, currentUser);
        
        updateWorkflowEntityFromDto(workflow, requestDto); // Use helper for mapping

        Workflow updatedWorkflow = workflowRepository.save(workflow);
        return mapToDto(updatedWorkflow);
    }

    @Transactional
    public void deleteWorkflow(Long workflowId, User currentUser) {
        Workflow workflow = findWorkflowByIdOrThrow(workflowId);
        checkOwnership(workflow, currentUser);
        workflowRepository.delete(workflow);
    }

    // --- Helper Methods ---

    private Workflow findWorkflowByIdOrThrow(Long workflowId) {
        return workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id: " + workflowId));
    }

    private void checkOwnership(Workflow workflow, User user) {
        if (!workflow.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("User does not have permission to access this workflow.");
        }
    }
    
    // Helper to map fields from Create DTO to entity
    private void updateWorkflowEntityFromDto(Workflow workflow, CreateWorkflowRequestDto dto) {
        workflow.setName(dto.getName());
        workflow.setDescription(dto.getDescription());
        workflow.setEnabled(dto.getEnabled());
        workflow.setTriggerConfig(dto.getTriggerConfig());
        workflow.setActionConfig(dto.getActionConfig());
    }

    // Overload helper to map fields from Update DTO to entity
    // Allows for partial updates if fields in Update DTO are nullable
    private void updateWorkflowEntityFromDto(Workflow workflow, UpdateWorkflowRequestDto dto) {
        if (dto.getName() != null) workflow.setName(dto.getName());
        // Allow setting description to null/empty if intended
        workflow.setDescription(dto.getDescription()); 
        if (dto.getEnabled() != null) workflow.setEnabled(dto.getEnabled());
        if (dto.getTriggerConfig() != null) workflow.setTriggerConfig(dto.getTriggerConfig());
        if (dto.getActionConfig() != null) workflow.setActionConfig(dto.getActionConfig());
    }

    private WorkflowDto mapToDto(Workflow workflow) {
        return new WorkflowDto(
                workflow.getId(),
                workflow.getName(),
                workflow.getDescription(),
                workflow.isEnabled(),
                workflow.getTriggerConfig(),
                workflow.getActionConfig(),
                workflow.getUser().getId(),
                workflow.getCreatedAt(),
                workflow.getUpdatedAt()
        );
    }
} 