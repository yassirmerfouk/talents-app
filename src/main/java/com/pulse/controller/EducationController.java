package com.pulse.controller;

import com.pulse.dto.Education.EducationRequest;
import com.pulse.dto.Education.EducationResponse;
import com.pulse.service.education.EducationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/educations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<EducationResponse> addEducation(
            @RequestBody @Valid EducationRequest educationRequest
            ){
        return new ResponseEntity<>(
                educationService.addEducation(educationRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<EducationResponse> updateEducation(
            @PathVariable Long id,
            @RequestBody @Valid EducationRequest educationRequest
    ){
        return new ResponseEntity<>(
                educationService.updateEducation(id, educationRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<?> deleteEducation(
            @PathVariable Long id
    ){
        educationService.deleteEducation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
