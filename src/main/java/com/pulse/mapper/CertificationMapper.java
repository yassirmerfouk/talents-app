package com.pulse.mapper;

import com.pulse.dto.certification.CertificationRequest;
import com.pulse.dto.certification.CertificationResponse;
import com.pulse.model.Certification;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CertificationMapper {

    public Certification mapToCertification(CertificationRequest certificationRequest){
        Certification certification = new Certification();
        BeanUtils.copyProperties(certificationRequest, certification);
        return certification;
    }

    public CertificationResponse mapToCertificationResponse(Certification certification){
        CertificationResponse certificationResponse = new CertificationResponse();
        BeanUtils.copyProperties(certification, certificationResponse);
        return certificationResponse;
    }
}
