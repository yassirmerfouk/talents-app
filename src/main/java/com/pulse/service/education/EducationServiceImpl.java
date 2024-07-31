package com.pulse.service.education;

import com.pulse.dto.Education.EducationRequest;
import com.pulse.dto.Education.EducationResponse;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.EducationMapper;
import com.pulse.model.Education;
import com.pulse.model.Talent;
import com.pulse.repository.EducationRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService{

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;
    private final AuthenticationService authenticationService;

    private Education getEducation(Long id){
        return educationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Education %d not found.",id))
        );
    }

    private Education getAuthorizedExperience(Long id){
        Education education = getEducation(id);
        if(!education.getTalent().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        return education;
    }

    @Override
    public EducationResponse addEducation(EducationRequest educationRequest){
        Talent talent = (Talent) authenticationService.getAuthenticatedUser();
        Education education = educationMapper.mapToEducation(educationRequest);
        education.setTalent(talent);
        educationRepository.save(education);
        return educationMapper.mapToEducationResponse(education);
    }

    @Override
    public EducationResponse updateEducation(Long id, EducationRequest educationRequest){
        Education education = getAuthorizedExperience(id);
        education.copyProperties(educationMapper.mapToEducation(educationRequest));
        educationRepository.save(education);
        return educationMapper.mapToEducationResponse(education);
    }

    @Override
    public void deleteEducation(Long id){
        Education education = getAuthorizedExperience(id);
        educationRepository.delete(education);
    }
}
