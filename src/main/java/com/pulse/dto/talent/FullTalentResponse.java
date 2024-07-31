package com.pulse.dto.talent;

import com.pulse.dto.Education.EducationResponse;
import com.pulse.dto.certification.CertificationResponse;
import com.pulse.dto.experience.ExperienceResponse;
import com.pulse.dto.language.LanguageResponse;
import com.pulse.dto.project.ProjectResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class FullTalentResponse extends TalentResponse{

    private List<ExperienceResponse> experiences;
    private List<EducationResponse> educations;
    private List<ProjectResponse> projects;
    private Set<String> skills;
    private List<CertificationResponse> certifications;
    private List<LanguageResponse> languages;
}
