package com.pulse.dto.client;

import com.pulse.enumeration.ClientType;
import com.pulse.enumeration.VerificationStatus;
import jakarta.persistence.Column;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ClientResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String image;
    private VerificationStatus status;
    private String companyName;
    private String sector;
    private String shortDescription;
    private String longDescription;
    private String city;
    private String country;
    private String website;
    private String size;
    private ClientType type;
}

