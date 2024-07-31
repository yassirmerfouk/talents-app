package com.pulse.service.skill;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.skill.SkillRequest;
import com.pulse.dto.skill.SkillResponse;
import com.pulse.dto.skill.SkillsRequest;
import com.pulse.model.Skill;

import java.util.List;
import java.util.Set;

public interface SkillService {
    Set<SkillResponse> addSkills(SkillsRequest skillsRequest);

    Set<Skill> addSkills(Set<String> titles);

    Skill addSkill(String title);

    Skill getSkillByTitle(String title);

    List<SkillResponse> getSkills();

    PageResponse<SkillResponse> getSkills(int page, int size);

    SkillResponse addSkill(SkillRequest skillRequest);

    SkillResponse updateSkill(Long id, SkillRequest skillRequest);

    void deleteSkill(Long id);
}
