package com.pulse.controller;

import com.pulse.dto.experience.ExperienceRequest;
import com.pulse.dto.experience.ExperienceResponse;
import com.pulse.service.experience.ExperienceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/experiences")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<ExperienceResponse> addExperience(
            @RequestBody @Valid ExperienceRequest experienceRequest
            ){
        return new ResponseEntity<>(
                experienceService.addExperience(experienceRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long id,
            @RequestBody @Valid ExperienceRequest experienceRequest
    ){
        return new ResponseEntity<>(
                experienceService.updateExperience(id, experienceRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<?> deleteExperience(
            @PathVariable Long id
    ){
        experienceService.deleteExperience(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
