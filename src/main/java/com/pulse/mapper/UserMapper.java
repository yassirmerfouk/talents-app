package com.pulse.mapper;

import com.pulse.dto.client.ClientRequest;
import com.pulse.dto.client.ClientResponse;
import com.pulse.dto.registration.ClientRegistrationRequest;
import com.pulse.dto.registration.TalentRegistrationRequest;
import com.pulse.dto.talent.FullTalentResponse;
import com.pulse.dto.talent.TalentRequest;
import com.pulse.dto.talent.TalentResponse;
import com.pulse.enumeration.VerificationStatus;
import com.pulse.model.Client;
import com.pulse.model.Skill;
import com.pulse.model.Talent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    @Value("${talents.images.url}")
    private String talentsImagesUrl;

    private final ExperienceMapper experienceMapper;
    private final EducationMapper educationMapper;
    private final ProjectMapper projectMapper;
    private final SkillMapper skillMapper;
    private final CertificationMapper certificationMapper;
    private final LanguageMapper languageMapper;

    public Client mapFromRegistrationToClient(ClientRegistrationRequest clientRegistrationRequest){
        Client client = new Client();
        BeanUtils.copyProperties(clientRegistrationRequest,client);
        client.setStatus(VerificationStatus.NOT_VERIFIED);
        return client;
    }

    public Talent mapFromRegistrationToTalent(TalentRegistrationRequest talentRegistrationRequest){
        Talent talent = new Talent();
        BeanUtils.copyProperties(talentRegistrationRequest, talent);
        talent.setStatus(VerificationStatus.NOT_VERIFIED);
        return talent;
    }

    public Client mapToClient(ClientRequest clientRequest){
        Client client = new Client();
        BeanUtils.copyProperties(clientRequest, client);
        return client;
    }

    public ClientResponse mapToClientResponse(Client client){
        ClientResponse clientResponse = new ClientResponse();
        BeanUtils.copyProperties(client, clientResponse);
        return clientResponse;
    }

    public Talent mapToTalent(TalentRequest talentRequest){
        Talent talent = new Talent();
        BeanUtils.copyProperties(talentRequest, talent);
        return talent;
    }

    public TalentResponse mapToTalentResponse(Talent talent){
        TalentResponse talentResponse = new TalentResponse();
        BeanUtils.copyProperties(talent, talentResponse);
        return talentResponse;
    }

    public FullTalentResponse mapToFullTalentResponse(Talent talent){
        FullTalentResponse fullTalentResponse = new FullTalentResponse();
        BeanUtils.copyProperties(talent, fullTalentResponse);
        fullTalentResponse.setExperiences(
                talent.getExperiences().stream()
                        .map(experienceMapper::mapToExperienceResponse).toList()
        );
        fullTalentResponse.setEducations(
                talent.getEducations().stream()
                        .map(educationMapper::mapToEducationResponse).toList()
        );
        fullTalentResponse.setProjects(
                talent.getProjects().stream()
                        .map(projectMapper::mapToProjectResponse).collect(Collectors.toList())
        );
        fullTalentResponse.setSkills(
                talent.getSkills().stream()
                        .map(Skill::getTitle).collect(Collectors.toSet())
        );
        fullTalentResponse.setCertifications(
                talent.getCertifications().stream()
                        .map(certificationMapper::mapToCertificationResponse).toList()
        );
        fullTalentResponse.setLanguages(
                talent.getLanguages().stream()
                        .map(languageMapper::mapToLanguageResponse).toList()
        );
        return fullTalentResponse;
    }
}
