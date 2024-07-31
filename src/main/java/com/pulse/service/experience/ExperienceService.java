package com.pulse.service.experience;

import com.pulse.dto.experience.ExperienceRequest;
import com.pulse.dto.experience.ExperienceResponse;

public interface ExperienceService {
    ExperienceResponse addExperience(ExperienceRequest experienceRequest);

    ExperienceResponse updateExperience(Long id, ExperienceRequest experienceRequest);

    void deleteExperience(Long id);
}
