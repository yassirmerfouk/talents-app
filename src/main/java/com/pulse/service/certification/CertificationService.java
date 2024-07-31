package com.pulse.service.certification;

import com.pulse.dto.certification.CertificationRequest;
import com.pulse.dto.certification.CertificationResponse;

public interface CertificationService {
    CertificationResponse addCertification(CertificationRequest certificationRequest);

    CertificationResponse updateCertification(Long id, CertificationRequest certificationRequest);

    void deleteCertification(Long id);
}
