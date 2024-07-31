package com.pulse.controller;

import com.pulse.dto.certification.CertificationRequest;
import com.pulse.dto.certification.CertificationResponse;
import com.pulse.service.certification.CertificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<CertificationResponse> addCertification(
            @RequestBody @Valid CertificationRequest certificationRequest
            ){
        return new ResponseEntity<>(
                certificationService.addCertification(certificationRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<CertificationResponse> updateCertification(
            @PathVariable Long id,
            @RequestBody @Valid CertificationRequest certificationRequest
    ){
        return new ResponseEntity<>(
                certificationService.updateCertification(id, certificationRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<?> deleteCertification(
            @PathVariable Long id
    ){
        certificationService.deleteCertification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
