package com.pulse.service.talent;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.skill.SkillsRequest;
import com.pulse.dto.talent.TalentRequest;
import com.pulse.dto.talent.TalentResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface TalentService {
    TalentResponse profile();

    TalentResponse updateProfile(TalentRequest talentRequest);

    Set<String> updateTalentSkills(SkillsRequest skillsRequest);

    TalentResponse getTalentById(Long id);

    PageResponse<TalentResponse> getTalents(String status, int page, int size);

    List<TalentResponse> getTalentsByListOfIds(List<Long> ids);
}
