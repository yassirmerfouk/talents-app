package com.pulse.controller;

import com.pulse.dto.language.LanguageRequest;
import com.pulse.dto.language.LanguageResponse;
import com.pulse.service.language.LanguageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<LanguageResponse> addLanguage(
            @RequestBody @Valid LanguageRequest languageRequest
            ){
        return new ResponseEntity<>(
                languageService.addLanguage(languageRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<LanguageResponse> updateLanguage(
            @PathVariable Long id,
            @RequestBody @Valid LanguageRequest languageRequest
    ){
        return new ResponseEntity<>(
                languageService.updateLanguage(id, languageRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<?> deleteLanguage(
            @PathVariable Long id
    ){
        languageService.deleteLanguage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
