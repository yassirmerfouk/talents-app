package com.pulse.dto.notification;

import com.pulse.dto.user.UserResponse;
import com.pulse.enumeration.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class NotificationResponse {
    private Long id;
    private String body;
    private NotificationType type;
    private Long relatedModel;
    private boolean seen;
    private boolean clicked;
    private LocalDateTime createdAt;

    private UserResponse sender;
    private UserResponse receiver;
}
