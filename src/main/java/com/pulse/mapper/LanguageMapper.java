package com.pulse.mapper;

import com.pulse.dto.language.LanguageRequest;
import com.pulse.dto.language.LanguageResponse;
import com.pulse.model.Language;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public Language mapToLanguage(LanguageRequest languageRequest){
        Language language = new Language();
        BeanUtils.copyProperties(languageRequest, language);
        return language;
    }

    public LanguageResponse mapToLanguageResponse(Language language){
        LanguageResponse languageResponse = new LanguageResponse();
        BeanUtils.copyProperties(language, languageResponse);
        return languageResponse;
    }
}
