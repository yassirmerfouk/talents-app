package com.pulse.service.skill;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.skill.SkillRequest;
import com.pulse.dto.skill.SkillResponse;
import com.pulse.dto.skill.SkillsRequest;
import com.pulse.exception.custom.CustomException;
import com.pulse.mapper.SkillMapper;
import com.pulse.model.Skill;
import com.pulse.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    @Override
    public Set<SkillResponse> addSkills(SkillsRequest skillsRequest) {
        return addSkills(skillsRequest.getSkills()).stream().map(skillMapper::mapToSkillResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Skill> addSkills(Set<String> titles) {
        return titles.stream().map(this::addSkill).collect(Collectors.toSet());
    }

    @Override
    public Skill addSkill(String title) {
        Skill skill = getSkillByTitle(title);
        if (skill == null)
            return skillRepository.save(Skill.builder().title(title).build());
        return skill;
    }

    @Override
    public Skill getSkillByTitle(String title) {
        return skillRepository.findByTitle(title).orElse(null);
    }

    @Override
    public List<SkillResponse> getSkills() {
        return skillRepository.findAll().stream().map(skill ->
                SkillResponse.builder().id(skill.getId()).title(skill.getTitle()).build()
        ).toList();
    }

    @Override
    public PageResponse<SkillResponse> getSkills(int page, int size) {
        Page<Skill> skillPage = skillRepository.findAll(
                PageRequest.of(page, size, Sort.by("id").ascending())
        );
        List<SkillResponse> skillResponses = skillPage.getContent().stream()
                .map(skillMapper::mapToSkillResponse).toList();
        return new PageResponse<>(skillResponses, page, size, skillPage.getTotalPages(), skillPage.getTotalElements());
    }

    @Override
    public SkillResponse addSkill(SkillRequest skillRequest) {
        if (skillRepository.existsByTitle(skillRequest.getTitle()))
            throw new CustomException("Skill already exists, please choose another title.");
        Skill skill = addSkill(skillRequest.getTitle());
        return skillMapper.mapToSkillResponse(skill);
    }

   /* public SkillResponse updateSkill(SkillRequest skillRequest){

    }*/
}
