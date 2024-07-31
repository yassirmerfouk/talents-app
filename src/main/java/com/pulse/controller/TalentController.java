package com.pulse.controller;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.skill.SkillsRequest;
import com.pulse.dto.talent.TalentRequest;
import com.pulse.dto.talent.TalentResponse;
import com.pulse.service.talent.TalentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/talents")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TalentController {

    private final TalentService talentService;

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<TalentResponse> profile(){
        return new ResponseEntity<>(
                talentService.profile(),
                HttpStatus.OK
        );
    }

    @PatchMapping("/profile")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<TalentResponse> updateProfile(
            @RequestBody @Valid TalentRequest talentRequest
            ){
        return new ResponseEntity<>(
                talentService.updateProfile(talentRequest),
                HttpStatus.OK
        );
    }

    @PatchMapping("/skills")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<?> updateTalentSkills(
            @RequestBody @Valid SkillsRequest skillsRequest
            ){
        return new ResponseEntity<>(
                talentService.updateTalentSkills(skillsRequest),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TalentResponse> getTalentById(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(
                talentService.getTalentById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<TalentResponse>> getTalents(
            @RequestParam(name = "status", defaultValue = "all") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                talentService.getTalents(status, page, size),
                HttpStatus.OK);
    }

}
