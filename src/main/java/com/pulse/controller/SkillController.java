package com.pulse.controller;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.skill.SkillRequest;
import com.pulse.dto.skill.SkillResponse;
import com.pulse.dto.skill.SkillsRequest;
import com.pulse.service.skill.SkillService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getSkills(){
        return new ResponseEntity<>(
                skillService.getSkills(),
                HttpStatus.OK
        );
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponse<SkillResponse>> getSkills(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                skillService.getSkills(page, size),
                HttpStatus.OK
        );
    }

    @PostMapping("/multiple")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Set<SkillResponse>> addSkills(
            @RequestBody SkillsRequest skillsRequest
            ){
        return new ResponseEntity<>(skillService.addSkills(skillsRequest), HttpStatus.CREATED);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SkillResponse> addSkill(
            @RequestBody @Valid SkillRequest skillRequest
            ){
        return new ResponseEntity<>(
                skillService.addSkill(skillRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SkillResponse> updateSkill(
            @PathVariable Long id,
            @RequestBody @Valid SkillRequest skillRequest
    ){
        return new ResponseEntity<>(
                skillService.updateSkill(id, skillRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteSkill(
            @PathVariable Long id
    ){
        skillService.deleteSkill(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
