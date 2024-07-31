package com.pulse.controller;

import com.pulse.dto.meet.MeetRequest;
import com.pulse.dto.meet.MeetResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.service.meet.MeetService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meets")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MeetController {

    private final MeetService meetService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MeetResponse> addMeet(
            @RequestBody @Valid MeetRequest meetRequest
            ) throws MessagingException {
        return new ResponseEntity<>(
                meetService.addMeet(meetRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> closeMeet(
            @PathVariable Long id
    ){
        meetService.closeMeet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<MeetResponse>> getMeets(
            @RequestParam(name = "date", defaultValue = "") String date,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                meetService.getMeets(date,page, size),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAnyAuthority('TALENT', 'CLIENT')")
    public ResponseEntity<?> acceptMeet(
            @PathVariable Long id
    ){
        meetService.acceptMeet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/refuse")
    @PreAuthorize("hasAnyAuthority('TALENT', 'CLIENT')")
    public ResponseEntity<?> refuseMeet(
            @PathVariable Long id
    ){
        meetService.refuseMeet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
