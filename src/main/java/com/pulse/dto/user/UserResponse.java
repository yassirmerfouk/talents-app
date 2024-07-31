package com.pulse.dto.user;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String image;
    private String type;
}
