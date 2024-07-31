package com.pulse.service.job;

import com.pulse.dto.appilication.ApplicationResponse;
import com.pulse.dto.job.JobRequest;
import com.pulse.dto.job.JobResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.enumeration.JobStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.ApplicationMapper;
import com.pulse.mapper.InterviewMapper;
import com.pulse.mapper.JobMapper;
import com.pulse.model.*;
import com.pulse.repository.*;
import com.pulse.service.authentication.AuthenticationService;
import com.pulse.service.notification.NotificationService;
import com.pulse.service.skill.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final CustomJobRepository customJobRepository;
    private final JobMapper jobMapper;
    private final AuthenticationService authenticationService;
    private final ApplicationRepository applicationRepository;

    private final ExperienceRepository experienceRepository;
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;

    private final JobInterviewRepository jobInterviewRepository;

    private final TalentRepository talentRepository;

    private final SkillService skillService;

    private final ApplicationMapper applicationMapper;
    private final InterviewMapper interviewMapper;

    private final NotificationService notificationService;

    private Job getJob(Long id) {
        return customJobRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Job %d not found.", id))
        );
    }

    private Job getAuthorizedJob(Long id) {
        Job job = getJob(id);
        if (!job.getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        return job;
    }

    @Override
    public JobResponse addJob(JobRequest jobRequest) {
        authenticationService.checkVerification();
        Client client = (Client) authenticationService.getAuthenticatedUser();
        Job job = jobMapper.mapToJob(jobRequest);
        job.setCreatedAt(LocalDateTime.now());
        job.setStatus(JobStatus.OPEN);
        job.setSkills(skillService.addSkills(jobRequest.getSkills()));
        job.setClient(client);
        customJobRepository.save(job);
        return jobMapper.mapToJobResponse(job);
    }

    @Override
    public JobResponse updateJob(Long id, JobRequest jobRequest) {
        authenticationService.checkVerification();
        Job job = getAuthorizedJob(id);
        if (job.getStatus() != JobStatus.OPEN)
            throw new ForbiddenException();
        job.copyProperties(jobMapper.mapToJob(jobRequest));
        job.setSkills(skillService.addSkills(jobRequest.getSkills()));
        customJobRepository.save(job);
        return jobMapper.mapToJobResponse(job);
    }

    @Override
    public void deleteJob(Long id) {
        authenticationService.checkVerification();
        Job job = getAuthorizedJob(id);
        if (job.getStatus() != JobStatus.OPEN)
            throw new ForbiddenException();
        customJobRepository.delete(job);
    }

    @Override
    public JobResponse getJobById(Long id) {
        Job job = getJob(id);
        if (job.getStatus() != JobStatus.OPEN)
            if (!authenticationService.isAuthenticated()
                    || authenticationService.hasAuthority("TALENT")
                    || (authenticationService.hasAuthority("CLIENT") && !job.getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
            )
                throw new ForbiddenException();
        JobResponse jobResponse = jobMapper.mapToJobResponse(job);
        jobResponse.setApplicationsNumber(applicationRepository.countByJobId(id));
        return jobResponse;
    }

    @Override
    public PageResponse<JobResponse> getJobs(String status, int page, int size) {
        Page<Job> jobPage;
        if (!authenticationService.isAuthenticated() || authenticationService.hasAuthority("TALENT"))
            jobPage = customJobRepository.findByStatus(
                    JobStatus.OPEN,
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );
        else {
            if (authenticationService.hasAuthority("CLIENT"))
                jobPage = customJobRepository.findByStatusAndClientIdNot(
                        JobStatus.OPEN, authenticationService.getAuthenticatedUserId(),
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            else {
                if (status.equals("all"))
                    jobPage = customJobRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
                else
                    jobPage = customJobRepository.findByStatus(
                            JobStatus.valueOf(status),
                            PageRequest.of(page, size, Sort.by("createdAt").descending()
                            ));
            }
        }
        List<JobResponse> jobResponses = jobPage.getContent().stream()
                .map(job -> {
                    JobResponse jobResponse = jobMapper.mapToJobResponse(job);
                    jobResponse.setApplicationsNumber(applicationRepository.countByJobId(job.getId()));
                    return jobResponse;
                }).toList();
        return new PageResponse<>(jobResponses, page, size, jobPage.getTotalPages(), jobPage.getTotalElements());
    }


    @Override
    public PageResponse<JobResponse> searchJobs(
            String keyword,
            int page,
            int size
    ) {
        Page<Job> jobPage = customJobRepository.searchJobs(
                keyword,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
        List<JobResponse> jobResponses = jobPage.getContent().stream()
                .map(job -> {
                    JobResponse jobResponse = jobMapper.mapToJobResponse(job);
                    jobResponse.setApplicationsNumber(applicationRepository.countByJobId(job.getId()));
                    if (applicationRepository.existsByJobIdAndTalentId(job.getId(), authenticationService.getAuthenticatedUserId()))
                        jobResponse.setApplied(true);
                    return jobResponse;
                }).toList();
        return new PageResponse<>(jobResponses, page, size, jobPage.getTotalPages(), jobPage.getTotalElements());
    }

    @Override
    public PageResponse<JobResponse> getJobsByClientId(Long clientId, int page, int size) {
        Page<Job> jobPage = customJobRepository.findByClientId(
                clientId,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
        List<JobResponse> jobResponses = jobPage.getContent().stream()
                .map(job -> {
                    JobResponse jobResponse = jobMapper.mapToJobResponse(job);
                    jobResponse.setApplicationsNumber(applicationRepository.countByJobId(job.getId()));
                    return jobResponse;
                }).toList();
        return new PageResponse<>(jobResponses, page, size, jobPage.getTotalPages(), jobPage.getTotalElements());
    }

    @Override
    public PageResponse<JobResponse> getAuthenticatedClientJobs(int page, int size) {
        return getJobsByClientId(authenticationService.getAuthenticatedUserId(), page, size);
    }

    @Override
    public void applyToJob(Long id) {
        authenticationService.checkVerification();
        Job job = getJob(id);
        if (job.getStatus() != JobStatus.OPEN)
            throw new CustomException("You can't apply to an none open job.");
        Talent talent = talentRepository.findById(authenticationService.getAuthenticatedUserId()).get();
        if (applicationRepository.existsByJobIdAndTalentId(job.getId(), talent.getId()))
            throw new CustomException("You already applied to this job.");
        Long experiencesScore = calculateTalentExperiencesScore(job, talent);
        Long projectsScore = calculateTalentProjectsScore(job, talent);
        Long skillsScore = calculateTalentSkillsScore(job, talent);
        Long score = experiencesScore + projectsScore + skillsScore;
        Application application = Application.builder()
                .experiencesScore(experiencesScore)
                .projectsScore(projectsScore)
                .skillsScore(skillsScore)
                .score(score).job(job).talent(talent).build();
        applicationRepository.save(application);
    }

    @Override
    public void askToStartSelection(Long id) {
        authenticationService.checkVerification();
        Job job = getAuthorizedJob(id);
        if (job.getStatus() != JobStatus.OPEN)
            throw new CustomException("You can't ask to start the selection for an none open job.");
        job.setStatus(JobStatus.WAITING);
        customJobRepository.save(job);
        notificationService.sendJobStartProcessRequestToAdmins(job);
    }

    @Override
    public void startSelection(Long id) {
        Job job = getJob(id);
        if (job.getStatus() != JobStatus.WAITING)
            throw new CustomException("You can't start selection for none waiting job.");
        job.setStatus(JobStatus.IN_SELECTION);
        customJobRepository.save(job);
        notificationService.sendJobStartSelectionToClient(job);
    }

    @Override
    public void startApproving(Long id) {
        Job job = getJob(id);
        if (job.getStatus() != JobStatus.IN_SELECTION)
            throw new CustomException("You can't start approving for none in selection job.");
        job.setStatus(JobStatus.IN_APPROVING);
        customJobRepository.save(job);
        notificationService.sendJobStartApprovingToClient(job);
    }

    @Override
    public void closeJob(Long id) {
        Job job = getJob(id);
        if (authenticationService.hasAuthority("CLIENT")) {
            if (!job.getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
                throw new ForbiddenException("You can't close this job, permission denied.");
            if (job.getStatus() != JobStatus.IN_APPROVING)
                throw new CustomException("You can't close this job for now.");
            job.setStatus(JobStatus.CLIENT_CLOSE);
            notificationService.sendJobClientCloseToAdmins(job);
        } else {
            if (job.getStatus() != JobStatus.CLIENT_CLOSE)
                throw new CustomException("You can't close this job for now.");
            job.setStatus(JobStatus.ADMIN_CLOSE);
            notificationService.sendJobAdminCloseToClient(job);
        }
        customJobRepository.save(job);
    }

    @Override
    public PageResponse<ApplicationResponse> getJobApplications(
            Long id,
            String status,
            int page,
            int size
    ) {
        Job job = getJob(id);
        Page<Application> applicationPage;
        if (status.equalsIgnoreCase("SELECTED"))
            applicationPage = applicationRepository.findByJobIdAndSelectedTrue(
                    job.getId(), PageRequest.of(page, size)
            );
        else if (status.equalsIgnoreCase("APPROVED"))
            applicationPage = applicationRepository.findByJobIdAndApprovedTrue(
                    job.getId(), PageRequest.of(page, size)
            );
        else
            applicationPage = applicationRepository.findByJobId(
                    job.getId(), PageRequest.of(page, size, Sort.by("score").descending())
            );
        List<ApplicationResponse> applicationResponses = applicationPage.getContent().stream()
                .map(application -> {
                    ApplicationResponse applicationResponse = applicationMapper.mapToApplicationResponse(application);
                    applicationResponse.setJobInterviews(
                            jobInterviewRepository.findByApplicationId(application.getId())
                                    .stream().map(interviewMapper::mapToJobInterviewResponse)
                                    .toList()
                    );
                    return applicationResponse;
                }).toList();
        return new PageResponse<>(applicationResponses, page, size, applicationPage.getTotalPages(), applicationPage.getTotalElements());
    }

    @Override
    public List<ApplicationResponse> getSelectedJobApplications(Long id) {
        Job job = getJob(id);
        if (authenticationService.hasAuthority("CLIENT")
                && !job.getClient().getId().equals(authenticationService.getAuthenticatedUserId())
        )
            throw new ForbiddenException();
        List<Application> applications = applicationRepository.findByJobIdAndSelectedTrue(id);
        return applications.stream().map(application -> {
            ApplicationResponse applicationResponse = applicationMapper.mapToApplicationResponse(application);
            applicationResponse.setJobInterviews(
                    jobInterviewRepository.findByApplicationId(application.getId())
                            .stream().map(interviewMapper::mapToJobInterviewResponse)
                            .toList()
            );
            return applicationResponse;
        }).toList();
    }

    @Override
    public void selectTalent(Long jobId, Long talentId) {
        Application application = applicationRepository.findByJobIdAndTalentId(jobId, talentId)
                .orElseThrow(() -> new CustomException("Not matched."));
        if (application.getJob().getStatus() != JobStatus.IN_SELECTION && application.getJob().getStatus() != JobStatus.IN_APPROVING)
            throw new CustomException("You can't select a talent for now.");
        application.setSelected(!application.isSelected());
        applicationRepository.save(application);
    }

    @Override
    public void approveTalent(Long jobId, Long talentId) {
        Application application = applicationRepository.findByJobIdAndTalentId(jobId, talentId)
                .orElseThrow(() -> new CustomException("Not matched."));
        if (!application.getJob().getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        if (application.getJob().getStatus() != JobStatus.IN_APPROVING)
            throw new CustomException("You can't approve a talent for an none in approving job.");
        if (!application.isSelected())
            throw new CustomException("Talent is not selected for this job.");
        application.setApproved(true);
        applicationRepository.save(application);
    }

    @Override
    public void refuseTalent(Long jobId, Long talentId) {
        Application application = applicationRepository.findByJobIdAndTalentId(jobId, talentId)
                .orElseThrow(() -> new CustomException("Not matched."));
        if (!application.getJob().getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        if (application.getJob().getStatus() != JobStatus.IN_APPROVING)
            throw new CustomException("You can't approve a talent for an none in approving job.");
        if (!application.isSelected())
            throw new CustomException("Talent is not selected for this job.");
        application.setRefused(true);
        applicationRepository.save(application);
    }

    private Long calculateTalentExperiencesScore(Job job, Talent talent) {
        if (talent.getExperiences() == null || talent.getExperiences().isEmpty())
            return 0L;
        return job.getSkills().stream()
                .mapToLong(
                        skill -> experienceRepository.countBySkill(talent.getId(), skill.getTitle()) * 10
                ).sum();
    }

    private Long calculateTalentProjectsScore(Job job, Talent talent) {
        if (talent.getProjects() == null || talent.getProjects().isEmpty())
            return 0L;
        return job.getSkills().stream()
                .mapToLong(
                        skill -> projectRepository.countBySkill(talent.getId(), skill.getTitle()) * 3
                ).sum();
    }

    private Long calculateTalentSkillsScore(Job job, Talent talent) {
        if (talent.getSkills() == null || talent.getSkills().isEmpty())
            return 0L;
        return job.getSkills().stream()
                .mapToLong(
                        skill -> talentRepository.hasSkill(talent.getId(), skill.getTitle()) ? 1 : 0
                ).sum();
    }

}
