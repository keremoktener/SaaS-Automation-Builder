package com.saasautomationbuilder.backend.service;

import com.saasautomationbuilder.backend.domain.User;
import com.saasautomationbuilder.backend.domain.Workflow;
import com.saasautomationbuilder.backend.dto.CreateWorkflowRequestDto;
import com.saasautomationbuilder.backend.dto.WorkflowDto;
import com.saasautomationbuilder.backend.repository.UserRepository;
import com.saasautomationbuilder.backend.repository.WorkflowRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Automatically injects dependencies via constructor
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

    // Updated: Create workflow for the provided User object
    @Transactional
    public WorkflowDto createWorkflow(CreateWorkflowRequestDto requestDto, User user) {
        Workflow workflow = new Workflow();
        workflow.setName(requestDto.getName());
        workflow.setDescription(requestDto.getDescription());
        workflow.setEnabled(requestDto.getEnabled());
        workflow.setTriggerConfig(requestDto.getTriggerConfig());
        workflow.setActionConfig(requestDto.getActionConfig());
        workflow.setUser(user);

        Workflow savedWorkflow = workflowRepository.save(workflow);
        return mapToDto(savedWorkflow);
    }

    // --- Helper Methods ---

    private WorkflowDto mapToDto(Workflow workflow) {
        return new WorkflowDto(
                workflow.getId(),
                workflow.getName(),
                workflow.getDescription(),
                workflow.isEnabled(),
                workflow.getTriggerConfig(),
                workflow.getActionConfig(),
                workflow.getUser().getId(), // Map user ID
                workflow.getCreatedAt(),
                workflow.getUpdatedAt()
        );
    }
} 