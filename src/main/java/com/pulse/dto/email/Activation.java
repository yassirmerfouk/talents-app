package com.pulse.dto.email;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Activation {

    private String email;
    private String fullName;
    private String confirmationCode;
}
