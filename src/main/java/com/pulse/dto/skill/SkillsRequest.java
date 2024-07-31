package com.pulse.dto.skill;

import lombok.*;

import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter
@Builder
public class SkillsRequest {

    Set<String> skills;
}
