package com.pulse.mapper;

import com.pulse.dto.experience.ExperienceRequest;
import com.pulse.dto.experience.ExperienceResponse;
import com.pulse.model.Experience;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {

    public Experience mapToExperience(ExperienceRequest experienceRequest){
        Experience experience = new Experience();
        BeanUtils.copyProperties(experienceRequest, experience);
        return experience;
    }

    public ExperienceResponse mapToExperienceResponse(Experience experience){
        ExperienceResponse experienceResponse = new ExperienceResponse();
        BeanUtils.copyProperties(experience, experienceResponse);
        return experienceResponse;
    }
}
