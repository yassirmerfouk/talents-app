package com.pulse.mapper;

import com.pulse.dto.Education.EducationRequest;
import com.pulse.dto.Education.EducationResponse;
import com.pulse.model.Education;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public Education mapToEducation(EducationRequest educationRequest){
        Education education = new Education();
        BeanUtils.copyProperties(educationRequest, education);
        return education;
    }

    public EducationResponse mapToEducationResponse(Education education){
        EducationResponse educationResponse = new EducationResponse();
        BeanUtils.copyProperties(education, educationResponse);
        return educationResponse;
    }
}
