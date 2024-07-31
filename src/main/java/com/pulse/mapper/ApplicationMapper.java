package com.pulse.mapper;

import com.pulse.dto.appilication.ApplicationResponse;
import com.pulse.model.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationMapper {

    private final UserMapper userMapper;

    public ApplicationResponse mapToApplicationResponse(Application application){
        return ApplicationResponse.builder()
                .id(application.getId())
                .score(application.getScore())
                .experiencesScore(application.getExperiencesScore())
                .projectsScore(application.getProjectsScore())
                .skillsScore(application.getSkillsScore())
                .selected(application.isSelected())
                .approved(application.isApproved())
                .refused(application.isRefused())
                .hasAdminMeet(application.isHasAdminMeet())
                .hasClientMeet(application.isHasClientMeet())
                .talent(userMapper.mapToTalentResponse(application.getTalent()))
                .jobId(application.getJob().getId())
                .build();
    }
}
