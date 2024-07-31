package com.pulse.controller;

import com.pulse.dto.authentication.AuthenticationRequest;
import com.pulse.dto.authentication.AuthenticationResponse;
import com.pulse.dto.registration.ClientRegistrationRequest;
import com.pulse.dto.registration.TalentRegistrationRequest;
import com.pulse.dto.reset.ResetPasswordRequest;
import com.pulse.service.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest
            ){
        return new ResponseEntity<>(
                authenticationService.authenticate(authenticationRequest),
                HttpStatus.OK
        );
    }

    @PostMapping("/client/register")
    public ResponseEntity<?> registerClient(
            @RequestBody @Valid ClientRegistrationRequest clientRegistrationRequest
            ) throws MessagingException {
        return new ResponseEntity<>(
                authenticationService.registerClient(clientRegistrationRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/talent/register")
    public ResponseEntity<?> registerTalent(
            @RequestBody @Valid TalentRegistrationRequest talentRegistrationRequest
            ) throws MessagingException{
        return new ResponseEntity<>(
                authenticationService.registerTalent(talentRegistrationRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/account/confirm")
    public ResponseEntity<?> confirmToken(
            @RequestParam(name = "token") String token
    ){
        authenticationService.confirmAccount(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> resetPassword(
            @RequestParam(name = "email") String email
    ) throws MessagingException{
        authenticationService.resetPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> confirmResetPassword(
            @RequestBody @Valid ResetPasswordRequest resetPasswordRequest
            ){
        authenticationService.confirmResetPassword(resetPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
