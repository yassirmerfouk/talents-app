package com.pulse.controller;

import com.pulse.dto.client.ClientRequest;
import com.pulse.dto.client.ClientResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.service.client.ClientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientResponse> profile(){
        return new ResponseEntity<>(
                clientService.profile(),
                HttpStatus.OK
        );
    }

    @PatchMapping("/profile")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientResponse> updateProfile(
            @RequestBody @Valid ClientRequest clientRequest
            ){
        return new ResponseEntity<>(clientService.updateProfile(clientRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ClientResponse>> getClients(
            @RequestParam(name = "status", defaultValue = "all") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                clientService.getClients(status, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(
                clientService.getClientById(id),
                HttpStatus.OK
        );
    }

}
