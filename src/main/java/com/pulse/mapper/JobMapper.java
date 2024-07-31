package com.pulse.mapper;

import com.pulse.dto.job.JobRequest;
import com.pulse.dto.job.JobResponse;
import com.pulse.model.Job;
import com.pulse.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JobMapper {

    private final UserMapper userMapper;

    public Job mapToJob(JobRequest jobRequest){
        Job job = new Job();
        BeanUtils.copyProperties(jobRequest, job);
        return job;
    }

    public JobResponse mapToJobResponse(Job job){
        JobResponse jobResponse = new JobResponse();
        BeanUtils.copyProperties(job, jobResponse);
        jobResponse.setSkills(job.getSkills().stream().map(Skill::getTitle).collect(Collectors.toSet()));
        jobResponse.setClient(userMapper.mapToClientResponse(job.getClient()));
        return jobResponse;
    }
}
