package com.pulse.controller;

import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final AuthenticationService authenticationService;

    @GetMapping("/profile/completion")
    @PreAuthorize("hasAnyAuthority('TALENT', 'CLIENT')")
    public ResponseEntity<?> getProfileCompletion(){
        return new ResponseEntity<>(authenticationService.getProfileCompletion(),HttpStatus.OK);
    }
}
