package com.pulse.mapper;

import com.pulse.dto.notification.NotificationResponse;
import com.pulse.dto.user.UserResponse;
import com.pulse.model.Client;
import com.pulse.model.Notification;
import com.pulse.model.Talent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse mapToNotificationResponse(Notification notification) {
        NotificationResponse notificationResponse = new NotificationResponse();
        BeanUtils.copyProperties(notification, notificationResponse);
        if (notification.getSender() != null)
            notificationResponse.setSender(
                    UserResponse.builder().id(notification.getSender().getId())
                            .firstName(notification.getSender().getFirstName())
                            .lastName(notification.getSender().getLastName())
                            .type(
                                    notification.getSender() instanceof Client ?
                                            "CLIENT" :
                                            notification.getSender() instanceof Talent ? "TALENT" : "ADMIN"
                            )
                            .image(notification.getSender().getImage())
                            .build()
            );
        notificationResponse.setReceiver(
                UserResponse.builder().id(notification.getReceiver().getId())
                        .firstName(notification.getReceiver().getFirstName())
                        .lastName(notification.getReceiver().getLastName())
                        .type(
                                notification.getReceiver() instanceof Client ?
                                        "CLIENT" :
                                        notification.getReceiver() instanceof Talent ? "TALENT" : "ADMIN"
                        )
                        .image(notification.getReceiver().getImage())
                        .build()
        );
        return notificationResponse;
    }
}
