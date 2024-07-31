package com.pulse.controller;

import com.pulse.dto.appilication.ApplicationResponse;
import com.pulse.dto.job.JobRequest;
import com.pulse.dto.job.JobResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.service.job.JobService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<JobResponse> addJob(
            @RequestBody @Valid JobRequest jobRequest
            ){
        return new ResponseEntity<>(
                jobService.addJob(jobRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestBody @Valid JobRequest jobRequest
    ){
        return new ResponseEntity<>(
                jobService.updateJob(id,jobRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<?> deleteJob(
            @PathVariable Long id
    ){
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(
                jobService.getJobById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<JobResponse>> getJobs(
            @RequestParam(name = "status", defaultValue = "all") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                jobService.getJobs(status,page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<JobResponse>> searchJob(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                jobService.searchJobs(keyword, page,size),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/apply")
    @PreAuthorize("hasAuthority('TALENT')")
    public ResponseEntity<?> applyToJob(
            @PathVariable Long id
    ){
        jobService.applyToJob(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<PageResponse<JobResponse>> getJobsByClientId(
            @PathVariable Long clientId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                jobService.getJobsByClientId(clientId, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/client/authenticated")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<PageResponse<JobResponse>> getAuthenticatedClientJobs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                jobService.getAuthenticatedClientJobs(page, size),
                HttpStatus.OK
        );
    }


    @PostMapping("/{id}/selection/ask")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<?> askToStartSelection(
            @PathVariable Long id
    ){
        jobService.askToStartSelection(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/selection/start")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> startSelection(
            @PathVariable Long id
    ){
        jobService.startSelection(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/approving/start")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> startApproving(
            @PathVariable Long id
    ){
        jobService.startApproving(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public ResponseEntity<?> closeJob(
            @PathVariable Long id
    ){
        jobService.closeJob(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/applications")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<ApplicationResponse>> getJobApplications(
            @PathVariable Long id,
            @RequestParam(name = "status", defaultValue = "") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                jobService.getJobApplications(id, status,page, size),
                HttpStatus.OK
        );
    }

    @PostMapping("/{jobId}/select/{talentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> selectTalent(
            @PathVariable Long jobId,
            @PathVariable Long talentId
    ){
        jobService.selectTalent(jobId, talentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{id}/applications/selected")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public ResponseEntity<List<ApplicationResponse>> getSelectedJobApplications(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(
                jobService.getSelectedJobApplications(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/{jobId}/approve/{talentId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<?> approveTalent(
            @PathVariable Long jobId,
            @PathVariable Long talentId
    ){
        jobService.approveTalent(jobId, talentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{jobId}/refuse/{talentId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<?> refuseTalent(
            @PathVariable Long jobId,
            @PathVariable Long talentId
    ){
        jobService.refuseTalent(jobId, talentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
