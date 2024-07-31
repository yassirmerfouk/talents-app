package com.pulse.service.education;

import com.pulse.dto.Education.EducationRequest;
import com.pulse.dto.Education.EducationResponse;

public interface EducationService {
    EducationResponse addEducation(EducationRequest educationRequest);

    EducationResponse updateEducation(Long id, EducationRequest educationRequest);

    void deleteEducation(Long id);
}
