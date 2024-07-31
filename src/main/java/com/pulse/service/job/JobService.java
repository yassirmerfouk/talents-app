package com.pulse.service.job;

import com.pulse.dto.appilication.ApplicationResponse;
import com.pulse.dto.job.JobRequest;
import com.pulse.dto.job.JobResponse;
import com.pulse.dto.page.PageResponse;

import java.util.List;

public interface JobService {
    JobResponse addJob(JobRequest jobRequest);

    JobResponse updateJob(Long id, JobRequest jobRequest);

    void deleteJob(Long id);

    JobResponse getJobById(Long id);

    PageResponse<JobResponse> getJobs(String status, int page, int size);

    PageResponse<JobResponse> searchJobs(
            String keyword,
            int page,
            int size
    );

    PageResponse<JobResponse> getJobsByClientId(Long clientId, int page, int size);

    PageResponse<JobResponse> getAuthenticatedClientJobs(int page, int size);

    void applyToJob(Long id);

    void askToStartSelection(Long id);

    void startSelection(Long id);

    void startApproving(Long id);

    void closeJob(Long id);

    PageResponse<ApplicationResponse> getJobApplications(
            Long id,
            String status,
            int page,
            int size
    );

    List<ApplicationResponse> getSelectedJobApplications(Long id);

    void selectTalent(Long jobId, Long talentId);

    void approveTalent(Long jobId, Long talentId);

    void refuseTalent(Long jobId, Long talentId);
}
