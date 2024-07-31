package com.pulse.mapper;

import com.pulse.dto.skill.SkillRequest;
import com.pulse.dto.skill.SkillResponse;
import com.pulse.model.Skill;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public Skill mapToSkill(SkillRequest skillRequest){
        Skill skill = new Skill();
        BeanUtils.copyProperties(skillRequest, skill);
        return skill;
    }

    public SkillResponse mapToSkillResponse(Skill skill){
        SkillResponse skillResponse = new SkillResponse();
        BeanUtils.copyProperties(skill, skillResponse);
        return skillResponse;
    }
}
