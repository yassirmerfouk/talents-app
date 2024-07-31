package com.pulse.service.language;

import com.pulse.dto.language.LanguageRequest;
import com.pulse.dto.language.LanguageResponse;

public interface LanguageService {
    LanguageResponse addLanguage(LanguageRequest languageRequest);

    LanguageResponse updateLanguage(Long id, LanguageRequest languageRequest);

    void  deleteLanguage(Long id);
}
