package com.pulse.dto.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter
public class LanguageResponse {

    private Long id;
    private String title;
    private String level;
}
