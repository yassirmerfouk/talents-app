package com.pulse.service.interview;

import com.pulse.dto.interview.JobInterviewRequest;
import com.pulse.dto.interview.JobInterviewResponse;
import com.pulse.enumeration.JobInterviewStatus;
import com.pulse.enumeration.JobStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.InterviewMapper;
import com.pulse.model.Application;
import com.pulse.model.JobInterview;
import com.pulse.repository.ApplicationRepository;
import com.pulse.repository.JobInterviewRepository;
import com.pulse.service.authentication.AuthenticationService;
import com.pulse.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobInterviewServiceImpl implements JobInterviewService {

    private final ApplicationRepository applicationRepository;
    private final JobInterviewRepository jobInterviewRepository;

    private final AuthenticationService authenticationService;

    private final InterviewMapper interviewMapper;

    private final NotificationService notificationService;

    @Override
    public JobInterviewResponse addJobInterview(Long applicationId, JobInterviewRequest jobInterviewRequest){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Application %d not found.", applicationId)));
        if(!application.getJob().getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException("You can't manage this job application, not yours.");
        if(application.getJob().getStatus() != JobStatus.IN_APPROVING)
            throw new CustomException("You can't ask for an interview for this job.");
        if(!application.isSelected())
            throw new CustomException("You can't ask for a an interview for a none selected talent.");
        JobInterview jobInterview = interviewMapper.mapToJobInterview(jobInterviewRequest);
        jobInterview.setStatus(JobInterviewStatus.CREATED);
        jobInterview.setApplication(application);
        jobInterviewRepository.save(jobInterview);
        application.setHasClientMeet(true);
        applicationRepository.save(application);
        notificationService.sendJobInterviewRequestToAdmins(application);
        return interviewMapper.mapToJobInterviewResponse(jobInterview);
    }

    @Override
    public void acceptJobInterview(Long id){
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Job interview %d not found.", id))
        );
        jobInterview.setStatus(JobInterviewStatus.ACCEPTED);
        jobInterviewRepository.save(jobInterview);
    }
}
