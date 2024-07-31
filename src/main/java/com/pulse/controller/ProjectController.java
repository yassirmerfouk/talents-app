package com.pulse.controller;

import com.pulse.dto.project.ProjectRequest;
import com.pulse.dto.project.ProjectResponse;
import com.pulse.service.project.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<ProjectResponse> addProject(
            @RequestBody @Valid ProjectRequest projectRequest
            ){
        return new ResponseEntity<>(
                projectService.addProject(projectRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestBody @Valid ProjectRequest projectRequest
    ){
        return new ResponseEntity<>(
                projectService.updateProject(id, projectRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long id
    ){
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
