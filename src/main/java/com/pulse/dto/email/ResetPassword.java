package com.pulse.dto.email;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ResetPassword {

    private String email;
    private String fullName;
    private String token;
}
