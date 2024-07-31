package com.pulse.service.interview;

import com.pulse.dto.interview.JobInterviewRequest;
import com.pulse.dto.interview.JobInterviewResponse;

public interface JobInterviewService {
    JobInterviewResponse addJobInterview(Long applicationId, JobInterviewRequest jobInterviewRequest);

    void acceptJobInterview(Long id);
}
