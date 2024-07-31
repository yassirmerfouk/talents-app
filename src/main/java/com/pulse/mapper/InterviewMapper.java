package com.pulse.mapper;

import com.pulse.dto.interview.JobInterviewRequest;
import com.pulse.dto.interview.JobInterviewResponse;
import com.pulse.model.JobInterview;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {

    public JobInterview mapToJobInterview(JobInterviewRequest jobInterviewRequest){
        JobInterview jobInterview = new JobInterview();
        BeanUtils.copyProperties(jobInterviewRequest, jobInterview);
        return jobInterview;
    }

    public JobInterviewResponse mapToJobInterviewResponse(JobInterview jobInterview){
        JobInterviewResponse jobInterviewResponse = new JobInterviewResponse();
        BeanUtils.copyProperties(jobInterview, jobInterviewResponse);
        return jobInterviewResponse;
    }
}
