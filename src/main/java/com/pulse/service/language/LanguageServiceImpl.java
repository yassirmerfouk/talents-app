package com.pulse.service.language;

import com.pulse.dto.language.LanguageRequest;
import com.pulse.dto.language.LanguageResponse;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.LanguageMapper;
import com.pulse.model.Language;
import com.pulse.model.Talent;
import com.pulse.repository.LanguageRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService{

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;
    private final AuthenticationService authenticationService;

    private Language getLanguage(Long id){
        return languageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Language %d not found.",id))
        );
    }

    private Language getAuthorizedLanguage(Long id){
        Language language = getLanguage(id);
        if(!language.getTalent().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        return language;
    }

    @Override
    public LanguageResponse addLanguage(LanguageRequest languageRequest){
        Talent talent = (Talent) authenticationService.getAuthenticatedUser();
        Language language = languageMapper.mapToLanguage(languageRequest);
        language.setTalent(talent);
        languageRepository.save(language);
        return languageMapper.mapToLanguageResponse(language);
    }

    @Override
    public LanguageResponse updateLanguage(Long id, LanguageRequest languageRequest){
        Language language = getAuthorizedLanguage(id);
        language.copyProperties(languageMapper.mapToLanguage(languageRequest));
        languageRepository.save(language);
        return languageMapper.mapToLanguageResponse(language);
    }

    @Override
    public void  deleteLanguage(Long id){
        Language language = getAuthorizedLanguage(id);
        languageRepository.delete(language);
    }
}
