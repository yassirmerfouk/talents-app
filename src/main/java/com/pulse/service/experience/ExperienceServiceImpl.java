package com.pulse.service.experience;

import com.pulse.dto.experience.ExperienceRequest;
import com.pulse.dto.experience.ExperienceResponse;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.ExperienceMapper;
import com.pulse.model.Experience;
import com.pulse.model.Talent;
import com.pulse.repository.ExperienceRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService{

    private final ExperienceRepository experienceRepository;
    private final AuthenticationService authenticationService;
    private final ExperienceMapper experienceMapper;

    private Experience getExperience(Long id){
        return experienceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Experience %d not found.",id))
        );
    }

    private Experience getAuthorizedExperience(Long id){
        Experience experience = getExperience(id);
        if(!experience.getTalent().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        return experience;
    }

    @Override
    public ExperienceResponse addExperience(ExperienceRequest experienceRequest){
        Talent talent = (Talent) authenticationService.getAuthenticatedUser();
        Experience experience = experienceMapper.mapToExperience(experienceRequest);
        experience.setTalent(talent);
        experienceRepository.save(experience);
        return experienceMapper.mapToExperienceResponse(experience);
    }

    @Override
    public ExperienceResponse updateExperience(Long id, ExperienceRequest experienceRequest){
        Experience experience = getAuthorizedExperience(id);
        experience.copyProperties(experienceMapper.mapToExperience(experienceRequest));
        experienceRepository.save(experience);
        return experienceMapper.mapToExperienceResponse(experience);
    }

    @Override
    public void deleteExperience(Long id){
        Experience experience = getAuthorizedExperience(id);
        experienceRepository.delete(experience);
    }
}
