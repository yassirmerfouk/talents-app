package com.pulse.service.notification;

import com.pulse.dto.notification.NotificationResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.model.*;

import java.util.List;

public interface NotificationService {
    NotificationResponse addNotification(Notification notification);

    void sendNotificationToUser(Notification notification);

    List<NotificationResponse> getUserNotifications();

    PageResponse<NotificationResponse> getUserNotifications(int page, int size);

    void readUserNotifications();

    void clickOnNotification(Long id);

    void deleteNotification(Long id);

    void sendNotificationToAllUsers();

    void sendMeetNotificationToUser(Meet meet);

    void sendVerificationRequestToAdmins(User user);

    void sendVerificationNotificationToUser(User user);

    void sendJobStartProcessRequestToAdmins(Job job);

    void sendJobStartSelectionToClient(Job job);

    void sendJobStartApprovingToClient(Job job);

    void sendJobClientCloseToAdmins(Job job);

    void sendJobAdminCloseToClient(Job job);

    void sendJobInterviewRequestToAdmins(Application application);
}
