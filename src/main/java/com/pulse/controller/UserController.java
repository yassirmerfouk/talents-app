package com.pulse.controller;

import com.pulse.service.user.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @PatchMapping("/verification/ask")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> askForVerification(){
        userService.askForVerification();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> verifyUser(
            @PathVariable Long id
    ){
        userService.verifyUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping ("/{id}/ban")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> banUser(
            @PathVariable Long id
    ){
        userService.banUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateImage(
          @Parameter @RequestPart MultipartFile image
    ){
        return new ResponseEntity<>(
                userService.updateImage(image),
                HttpStatus.OK
        );
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getTalentImage(
            @PathVariable String imageName
    ){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", imageName);
        return new ResponseEntity<>(
                userService.getImage(imageName),
                headers, HttpStatus.OK);
    }
}
