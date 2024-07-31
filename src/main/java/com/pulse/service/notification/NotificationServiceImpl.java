package com.pulse.service.notification;

import com.pulse.dto.notification.NotificationResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.enumeration.MeetType;
import com.pulse.enumeration.NotificationType;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.mapper.NotificationMapper;
import com.pulse.model.*;
import com.pulse.repository.AdminRepository;
import com.pulse.repository.NotificationRepository;
import com.pulse.repository.UserRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    private final AdminRepository adminRepository;

    @Value("${notification.message.test}")
    private String testMessage;
    @Value("${notification.message.meet}")
    private String meetMessage;
    @Value("${notification.message.verification-request}")
    private String verificationRequestMessage;
    @Value("${notification.message.verification-confirmation}")
    private String verificationConfirmationMessage;
    @Value("${notification.message.job-start-process-request}")
    private String jobStartProcessRequestMessage;
    @Value("${notification.message.job-start-selection}")
    private String jobStartSelectionMessage;
    @Value("${notification.message.job-start-approving}")
    private String jobStartApprovingMessage;
    @Value("${notification.message.job-client-close}")
    private String jobClientCloseMessage;
    @Value("${notification.message.job-admin-close}")
    private String jobAdminCloseMessage;
    @Value("${notification.message.job-interview-request}")
    private String jobInterviewRequestMessage;

    @Override
    public NotificationResponse addNotification(Notification notification) {
        notificationRepository.save(notification);
        return notificationMapper.mapToNotificationResponse(notification);
    }

    @Override
    public void sendNotificationToUser(Notification notification) {
        NotificationResponse notificationResponse = addNotification(notification);
        messagingTemplate.convertAndSend("/topic/notifications/specific-" + notification.getReceiver().getId(), notificationResponse);
    }

    @Override
    public List<NotificationResponse> getUserNotifications() {
        Page<Notification> notificationPage = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(
                authenticationService.getAuthenticatedUserId(),
                PageRequest.of(0, 10)
        );
        return notificationPage.getContent()
                .stream().map(notificationMapper::mapToNotificationResponse)
                .toList();
    }

    @Override
    public PageResponse<NotificationResponse> getUserNotifications(int page, int size) {
        Page<Notification> notificationPage = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(
                authenticationService.getAuthenticatedUserId(),
                PageRequest.of(page, size)
        );
        List<NotificationResponse> notificationResponses = notificationPage.getContent().stream()
                .map(notificationMapper::mapToNotificationResponse).toList();
        return new PageResponse<>(notificationResponses, page, size, notificationPage.getTotalPages(), notificationPage.getTotalElements());
    }

    @Override
    public void readUserNotifications() {
        notificationRepository.findByReceiverIdAndSeenFalse(authenticationService.getAuthenticatedUserId())
                .forEach(notification -> {
                    notification.setSeen(true);
                    notificationRepository.save(notification);
                });
    }

    @Override
    public void clickOnNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Notification %d not found.", id)));
        notification.setClicked(true);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Notification %d not found.", id)));
        notificationRepository.delete(notification);
    }

    @Override
    public void sendNotificationToAllUsers() {
        userRepository.findAll().forEach(user -> {
            Notification notification = Notification.builder()
                    .body(testMessage)
                    .type(NotificationType.TEST)
                    .relatedModel(0L)
                    .receiver(user)
                    .createdAt(LocalDateTime.now())
                    .build();
            sendNotificationToUser(notification);
        });
    }

    @Override
    public void sendMeetNotificationToUser(Meet meet) {
        meet.getReceivers().forEach(receiver -> {
            String body = meetMessage.replace("[MEET TYPE]", meet.getMeetType() == MeetType.VERIFICATION ? "verification" : "interview")
                    .replace("[DATE]", meet.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm a")));
            Notification notification = Notification.builder()
                    .type(NotificationType.MEET)
                    .createdAt(LocalDateTime.now())
                    .relatedModel(meet.getId())
                    .receiver(receiver)
                    .body(body).build();
            sendNotificationToUser(notification);
        });
    }

    @Override
    public void sendVerificationRequestToAdmins(User user) {
        String body = verificationRequestMessage.replace("[USER TYPE]", user instanceof Talent ? "Talent" : "Client")
                .replace("[FIRST NAME]", user.getFirstName())
                .replace("[LAST NAME]", user.getLastName());
        adminRepository.findAll().forEach(admin -> {
            Notification notification = Notification.builder()
                    .type(user instanceof Talent ? NotificationType.TALENT_VERIFICATION : NotificationType.CLIENT_VERIFICATION)
                    .createdAt(LocalDateTime.now())
                    .relatedModel(user.getId())
                    .sender(user)
                    .receiver(admin)
                    .body(body).build();
            sendNotificationToUser(notification);
        });
    }

    @Override
    public void sendVerificationNotificationToUser(User user) {
        Notification notification = Notification.builder()
                .type(user instanceof Talent ? NotificationType.TALENT_VERIFICATION : NotificationType.CLIENT_VERIFICATION)
                .createdAt(LocalDateTime.now())
                .relatedModel(user.getId())
                .receiver(user)
                .body(verificationConfirmationMessage).build();
        sendNotificationToUser(notification);
    }

    @Override
    public void sendJobStartProcessRequestToAdmins(Job job){
        String body = jobStartProcessRequestMessage.replace("[FIRST NAME]", job.getClient().getFirstName())
                        .replace("[LAST NAME]", job.getClient().getLastName());
        adminRepository.findAll().forEach(admin -> {
            Notification notification = Notification.builder()
                    .type(NotificationType.JOB_STATUS_CHANGED)
                    .createdAt(LocalDateTime.now())
                    .relatedModel(job.getId())
                    .sender(job.getClient())
                    .receiver(admin)
                    .body(body).build();
            sendNotificationToUser(notification);
        });
    }

    @Override
    public void sendJobStartSelectionToClient(Job job){
        Notification notification = Notification.builder()
                .type(NotificationType.JOB_STATUS_CHANGED)
                .createdAt(LocalDateTime.now())
                .relatedModel(job.getId())
                .receiver(job.getClient())
                .body(jobStartSelectionMessage).build();
        sendNotificationToUser(notification);
    }

    @Override
    public void sendJobStartApprovingToClient(Job job){
        Notification notification = Notification.builder()
                .type(NotificationType.JOB_STATUS_CHANGED)
                .createdAt(LocalDateTime.now())
                .relatedModel(job.getId())
                .receiver(job.getClient())
                .body(jobStartApprovingMessage).build();
        sendNotificationToUser(notification);
    }

    @Override
    public void sendJobClientCloseToAdmins(Job job){
        String body = jobClientCloseMessage.replace("[FIRST NAME]", job.getClient().getFirstName())
                .replace("[LAST NAME]", job.getClient().getLastName());
        adminRepository.findAll().forEach(admin -> {
            Notification notification = Notification.builder()
                    .type(NotificationType.JOB_STATUS_CHANGED)
                    .createdAt(LocalDateTime.now())
                    .relatedModel(job.getId())
                    .sender(job.getClient())
                    .receiver(admin)
                    .body(body).build();
            sendNotificationToUser(notification);
        });
    }

    @Override
    public void sendJobAdminCloseToClient(Job job){
        Notification notification = Notification.builder()
                .type(NotificationType.JOB_STATUS_CHANGED)
                .createdAt(LocalDateTime.now())
                .relatedModel(job.getId())
                .receiver(job.getClient())
                .body(jobAdminCloseMessage).build();
        sendNotificationToUser(notification);
    }

    @Override
    public void sendJobInterviewRequestToAdmins(Application application){
        String body = jobInterviewRequestMessage.replace("[FIRST NAME]", application.getJob().getClient().getFirstName())
                .replace("[LAST NAME]", application.getJob().getClient().getLastName())
                        .replace("[FIRST NAME 2]", application.getTalent().getFirstName())
                .replace("[LAST NAME 2]", application.getTalent().getLastName());
        adminRepository.findAll().forEach(admin -> {
            Notification notification = Notification.builder()
                    .type(NotificationType.INTERVIEW_REQUEST)
                    .createdAt(LocalDateTime.now())
                    .relatedModel(application.getJob().getId())
                    .sender(application.getJob().getClient())
                    .receiver(admin)
                    .body(body).build();
            sendNotificationToUser(notification);
        });
    }
}
