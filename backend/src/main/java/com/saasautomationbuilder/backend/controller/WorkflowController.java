package com.saasautomationbuilder.backend.controller;

import com.saasautomationbuilder.backend.domain.User;
import com.saasautomationbuilder.backend.dto.CreateWorkflowRequestDto;
import com.saasautomationbuilder.backend.dto.UpdateWorkflowRequestDto;
import com.saasautomationbuilder.backend.dto.WorkflowDto;
import com.saasautomationbuilder.backend.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workflows") // Base path for workflow endpoints
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    // GET /api/v1/workflows
    // Get workflows for the currently authenticated user
    @GetMapping
    public ResponseEntity<List<WorkflowDto>> getWorkflowsForCurrentUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal(); // Get User object from principal
        List<WorkflowDto> workflows = workflowService.getWorkflowsByUserId(currentUser.getId());
        return ResponseEntity.ok(workflows);
    }

    // POST /api/v1/workflows
    // Create workflow for the currently authenticated user
    @PostMapping
    public ResponseEntity<WorkflowDto> createWorkflow(
            @Valid @RequestBody CreateWorkflowRequestDto requestDto,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        WorkflowDto createdWorkflow = workflowService.createWorkflow(requestDto, currentUser); // Pass User object
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkflow);
    }

    // GET /api/v1/workflows/{workflowId}
    @GetMapping("/{workflowId}")
    public ResponseEntity<WorkflowDto> getWorkflowById(
            @PathVariable Long workflowId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        WorkflowDto workflow = workflowService.getWorkflowById(workflowId, currentUser);
        return ResponseEntity.ok(workflow);
    }

    // PUT /api/v1/workflows/{workflowId}
    @PutMapping("/{workflowId}")
    public ResponseEntity<WorkflowDto> updateWorkflow(
            @PathVariable Long workflowId,
            @Valid @RequestBody UpdateWorkflowRequestDto requestDto,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        WorkflowDto updatedWorkflow = workflowService.updateWorkflow(workflowId, requestDto, currentUser);
        return ResponseEntity.ok(updatedWorkflow);
    }

    // DELETE /api/v1/workflows/{workflowId}
    @DeleteMapping("/{workflowId}")
    public ResponseEntity<Void> deleteWorkflow(
            @PathVariable Long workflowId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        workflowService.deleteWorkflow(workflowId, currentUser);
        return ResponseEntity.noContent().build();
    }
} 