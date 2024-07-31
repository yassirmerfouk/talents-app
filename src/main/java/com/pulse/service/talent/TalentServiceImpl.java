package com.pulse.service.talent;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.skill.SkillsRequest;
import com.pulse.dto.talent.TalentRequest;
import com.pulse.dto.talent.TalentResponse;
import com.pulse.enumeration.VerificationStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.UserMapper;
import com.pulse.model.Skill;
import com.pulse.model.Talent;
import com.pulse.repository.TalentRepository;
import com.pulse.repository.UserRepository;
import com.pulse.service.authentication.AuthenticationService;
import com.pulse.service.skill.SkillService;
import com.pulse.service.storage.FileStorageService;
import com.pulse.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements TalentService{

    private final UserRepository userRepository;
    private final TalentRepository talentRepository;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    public final FileValidator fileValidator;
    private final FileStorageService fileStorageService;
    private final SkillService skillService;

    @Value("${talents.images.url}")
    private String talentsImagesUrl;

    @Override
    public TalentResponse profile(){
        Talent talent = talentRepository.findById(authenticationService.getAuthenticatedUserId()).get();
        return userMapper.mapToFullTalentResponse(talent);
    }

    @Override
    public TalentResponse updateProfile(TalentRequest talentRequest){
        Talent talent = talentRepository.findById(authenticationService.getAuthenticatedUserId()).get();
        if(userRepository.checkEmailAvailability(talent.getId(), talentRequest.getEmail()))
            throw new CustomException("Email already used by another user.");
        talent.copyProperties(userMapper.mapToTalent(talentRequest));
        talentRepository.save(talent);
        return userMapper.mapToFullTalentResponse(talent);
    }

    @Override
    public Set<String> updateTalentSkills(SkillsRequest skillsRequest){
        Talent talent = (Talent) authenticationService.getAuthenticatedUser();
        Set<Skill> skills = skillService.addSkills(skillsRequest.getSkills());
        talent.setSkills(skills);
        talentRepository.save(talent);
        return skills.stream().map(Skill::getTitle).collect(Collectors.toSet());
    }

    @Override
    public TalentResponse getTalentById(Long id){
        Talent talent = talentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Talent %d not found.",id))
        );
        if(talent.getStatus() != VerificationStatus.VERIFIED)
            if(!authenticationService.isAuthenticated() || !authenticationService.hasAuthority("ADMIN"))
                throw new ForbiddenException();
        return userMapper.mapToFullTalentResponse(talent);
    }

    @Override
    public PageResponse<TalentResponse> getTalents(String status, int page, int size){
        Page<Talent> talentPage;
        if(!authenticationService.isAuthenticated())
            talentPage = talentRepository.findByStatusAndEnabledTrue(
                    VerificationStatus.VERIFIED, PageRequest.of(page, size)
            );
        else{
            if(authenticationService.hasAuthority("TALENT") || authenticationService.hasAuthority("CLIENT"))
                talentPage = talentRepository.findByStatusAndIdNotAndEnabledTrue(
                        VerificationStatus.VERIFIED, authenticationService.getAuthenticatedUserId(),PageRequest.of(page, size)
                );
            else{
                if(status.equals("all"))
                    talentPage = talentRepository.findByEnabledTrue(PageRequest.of(page, size));
                else
                    talentPage = talentRepository.findByStatusAndEnabledTrue(
                            VerificationStatus.valueOf(status), PageRequest.of(page, size)
                    );
            }
        }
        List<TalentResponse> talentResponses = talentPage.getContent().stream()
                .map(userMapper::mapToTalentResponse).toList();
        return new PageResponse<>(talentResponses, page, size, talentPage.getTotalPages(), talentPage.getTotalElements());
    }

}
