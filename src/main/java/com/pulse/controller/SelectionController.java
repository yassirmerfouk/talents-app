package com.pulse.controller;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.selection.SelectionRequest;
import com.pulse.dto.selection.SelectionResponse;
import com.pulse.service.selection.SelectionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/selections")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SelectionController {

    private final SelectionService selectionService;

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SelectionResponse> addSelection(
            @RequestBody @Valid SelectionRequest selectionRequest
            ){
        return new ResponseEntity<>(
                selectionService.addSelection(selectionRequest),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<SelectionResponse>> getSelections(
            @RequestParam(name = "status", defaultValue = "ALL") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                selectionService.getSelections(status, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<SelectionResponse>> getSelectionsByClientId(
            @PathVariable Long clientId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                selectionService.getSelectionsByClientId(clientId, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/authenticated")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<PageResponse<SelectionResponse>> getAuthenticatedClientSelections(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                selectionService.getAuthenticatedClientSelections(page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public ResponseEntity<SelectionResponse> getSelectionById(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(
                selectionService.getSelectionById(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> acceptSelection(
            @PathVariable Long id
    ){
        selectionService.acceptSelection(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/refuse")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> refuseSelection(
            @PathVariable Long id
    ){
        selectionService.refuseSelection(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<?> deleteSelection(
            @PathVariable Long id
    ){
        selectionService.deleteSelection(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
