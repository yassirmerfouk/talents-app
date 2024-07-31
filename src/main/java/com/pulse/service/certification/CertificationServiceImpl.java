package com.pulse.service.certification;

import com.pulse.dto.certification.CertificationRequest;
import com.pulse.dto.certification.CertificationResponse;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.CertificationMapper;
import com.pulse.model.Certification;
import com.pulse.model.Talent;
import com.pulse.repository.CertificationRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService{

    private final CertificationRepository certificationRepository;
    private final CertificationMapper certificationMapper;
    private final AuthenticationService authenticationService;

    private Certification getCertification(Long id){
        return certificationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Certification %d not found.",id))
        );
    }

    private Certification getAuthorizedCertification(Long id){
        Certification certification = getCertification(id);
        if(!certification.getTalent().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        return certification;
    }

    @Override
    public CertificationResponse addCertification(CertificationRequest certificationRequest){
        Talent talent = (Talent) authenticationService.getAuthenticatedUser();
        Certification certification = certificationMapper.mapToCertification(certificationRequest);
        certification.setTalent(talent);
        certificationRepository.save(certification);
        return certificationMapper.mapToCertificationResponse(certification);
    }

    @Override
    public CertificationResponse updateCertification(Long id, CertificationRequest certificationRequest){
        Certification certification = getAuthorizedCertification(id);
        certification.copyProperties(certificationMapper.mapToCertification(certificationRequest));
        certificationRepository.save(certification);
        return certificationMapper.mapToCertificationResponse(certification);
    }

    @Override
    public void deleteCertification(Long id){
        Certification certification = getAuthorizedCertification(id);
        certificationRepository.delete(certification);
    }
}
