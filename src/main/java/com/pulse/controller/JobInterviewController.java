package com.pulse.controller;

import com.pulse.dto.interview.JobInterviewRequest;
import com.pulse.dto.interview.JobInterviewResponse;
import com.pulse.service.interview.JobInterviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-interviews")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class JobInterviewController {

    private final JobInterviewService jobInterviewService;

    @PostMapping("/{applicationId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<JobInterviewResponse> addJobInterview(
            @PathVariable Long applicationId,
            @RequestBody @Valid JobInterviewRequest jobInterviewRequest
            ){
        return new ResponseEntity<>(
                jobInterviewService.addJobInterview(applicationId, jobInterviewRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/accept/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> acceptJobInterview(
            @PathVariable Long id
    ){
        jobInterviewService.acceptJobInterview(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
