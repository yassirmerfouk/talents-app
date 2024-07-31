package com.pulse.dto.authentication;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
}
