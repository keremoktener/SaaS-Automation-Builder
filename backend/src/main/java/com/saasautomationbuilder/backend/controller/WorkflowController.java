package com.saasautomationbuilder.backend.controller;

import com.saasautomationbuilder.backend.dto.CreateWorkflowRequestDto;
import com.saasautomationbuilder.backend.dto.WorkflowDto;
import com.saasautomationbuilder.backend.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workflows") // Base path for workflow endpoints
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    // GET /api/v1/workflows/user/{userId}
    // Temporary endpoint using userId in path - replace with authenticated user later
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkflowDto>> getWorkflowsByUserId(@PathVariable Long userId) {
        List<WorkflowDto> workflows = workflowService.getWorkflowsByUserId(userId);
        return ResponseEntity.ok(workflows);
    }

    // POST /api/v1/workflows/user/{userId}
    // Temporary endpoint using userId in path - replace with authenticated user later
    @PostMapping("/user/{userId}")
    public ResponseEntity<WorkflowDto> createWorkflow(
            @PathVariable Long userId,
            @Valid @RequestBody CreateWorkflowRequestDto requestDto) {
        WorkflowDto createdWorkflow = workflowService.createWorkflow(requestDto, userId);
        // Return 201 Created status with the created workflow DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkflow);
    }

    // TODO:
    // - GET /api/v1/workflows/{workflowId} (Get specific workflow)
    // - PUT /api/v1/workflows/{workflowId} (Update workflow)
    // - DELETE /api/v1/workflows/{workflowId} (Delete workflow)
    // - Replace {userId} path params with logic based on authenticated user principal
} 