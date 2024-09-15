package com.pulse.service.meet;

import com.pulse.dto.meet.MeetRequest;
import com.pulse.dto.meet.MeetResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.enumeration.MeetStatus;
import com.pulse.enumeration.MeetType;
import com.pulse.enumeration.VerificationStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.mapper.MeetMapper;
import com.pulse.model.Application;
import com.pulse.model.Meet;
import com.pulse.model.User;
import com.pulse.repository.ApplicationRepository;
import com.pulse.repository.CustomJobRepository;
import com.pulse.repository.MeetRepository;
import com.pulse.repository.UserRepository;
import com.pulse.service.authentication.AuthenticationService;
import com.pulse.service.email.EmailService;
import com.pulse.service.notification.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;
    private final UserRepository userRepository;
    private final CustomJobRepository customJobRepository;
    private final ApplicationRepository applicationRepository;
    private final MeetMapper meetMapper;

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    private final NotificationService notificationService;

    public Meet getMeet(Long id) {
        return meetRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Meet %d not found.", id))
        );
    }

    private List<User> verifyReceivers(MeetRequest meetRequest) {
        // VERIFY IF ALL RECEIVERS EXISTS
        return meetRequest.getReceivers().stream().map(
                receiverId -> userRepository.findById(receiverId).orElseThrow(
                        () -> new EntityNotFoundException(String.format("user %d not found", receiverId))
                )
        ).toList();
    }

    private void verifySender(MeetRequest meetRequest){
        // VERIFY IF THE SENDER PUTS HIMSELF WITH THE RECEIVERS
        long counter = meetRequest.getReceivers().stream().filter(
                receiverId -> receiverId.equals(authenticationService.getAuthenticatedUserId())
        ).count();
        if (counter > 0)
            throw new CustomException("You can't send meet to yourself.");
    }

    private Meet createMeet(MeetRequest meetRequest, List<User> receivers){
        Meet meet = meetMapper.mapToMeet(meetRequest);
        meet.setStatus(MeetStatus.CREATED);
        meet.setSender(authenticationService.getAuthenticatedUser());
        meet.setReceivers(receivers);
        meetRepository.save(meet);
        notificationService.sendMeetNotificationToUser(meet);
        return meet;
    }

    @Override
    public MeetResponse addMeet(MeetRequest meetRequest) throws MessagingException {

        List<User> receivers = verifyReceivers(meetRequest);
        verifySender(meetRequest);

        Application application = null;
        // VERIFICATION MEET CASE :
        if (meetRequest.getMeetType() == MeetType.VERIFICATION) {
            // IN CASE OF VERIFICATION WE SHOULD HAVE ONLY ONE RECEIVER AND SHOULD THE RECEIVER BE A TALENT
            if (receivers.size() > 1)
                throw new CustomException("For a verification meeting you can't send a meet to multiple users.");
            else if (receivers.get(0).getStatus() != VerificationStatus.WAITING)
                throw new CustomException("You can't send a verification meeting to an none waiting user.");
        } else {
            if (meetRequest.getMeetType() == MeetType.ADMIN_INTERVIEW) {
                if (receivers.size() > 1)
                    throw new CustomException("For a admin interview you can't send a meet to multiple users.");
                if (!receivers.get(0).hasAuthority("TALENT"))
                    throw new CustomException("You can't create an admin interview to an none talent users.");
                if (receivers.get(0).getStatus() != VerificationStatus.VERIFIED)
                    throw new CustomException("You can't create a admin interview to an none verified talent.");
                application = applicationRepository.findByJobIdAndTalentId(
                        meetRequest.getJobId(), receivers.get(0).getId()
                ).orElseThrow(() -> new EntityNotFoundException("Application not found."));
                application.setHasAdminMeet(true);
            } else if (meetRequest.getMeetType() == MeetType.CLIENT_INTERVIEW) {
                if (receivers.size() < 2)
                    throw new CustomException("For a client interview the receivers should be both client and talent.");
                // CHECK IF ONE OF THE RECEIVERS OR ALL OF THEM ARE NOT VERIFIED
                receivers.forEach(receiver -> {
                    if (receiver.getStatus() != VerificationStatus.VERIFIED)
                        throw new CustomException("You can't create client interview meeting for none verified users.");
                });
            }
        }
        Meet meet = meetMapper.mapToMeet(meetRequest);
        meet.setStatus(MeetStatus.CREATED);
        meet.setSender(authenticationService.getAuthenticatedUser());
        meet.setReceivers(receivers);
        meetRepository.save(meet);
        if (application != null)
            applicationRepository.save(application);
        notificationService.sendMeetNotificationToUser(meet);
        return meetMapper.mapToMeetResponse(meet);
    }

    @Override
    public MeetResponse addSelectionMeet(MeetRequest meetRequest){
        List<User> receivers = verifyReceivers(meetRequest);
        verifySender(meetRequest);
        Meet meet = createMeet(meetRequest, receivers);
        return meetMapper.mapToMeetResponse(meet);
    }

    @Override
    public void closeMeet(Long id) {
        Meet meet = getMeet(id);
        meet.setStatus(MeetStatus.CLOSED);
        meetRepository.save(meet);
    }

    @Override
    public PageResponse<MeetResponse> getMeets(String strDate, int page, int size) {
        Page<Meet> meetPage;
        LocalDate dayDate = null;
        LocalDateTime startOfDay = null, endOfDay = null;
        if (!strDate.isEmpty()) {
            dayDate = LocalDate.parse(strDate);
            startOfDay = dayDate.atStartOfDay();
            endOfDay = dayDate.atTime(LocalTime.MAX);
        }
        if (authenticationService.hasAuthority("ADMIN")) {
            if (strDate.isEmpty())
                meetPage = meetRepository.findBySenderId(
                        authenticationService.getAuthenticatedUserId(),
                        PageRequest.of(page, size, Sort.by("date").descending())
                );
            else
                meetPage = meetRepository.findBySenderIdAndDateBetween(
                        authenticationService.getAuthenticatedUserId(),
                        startOfDay, endOfDay,
                        PageRequest.of(page, size, Sort.by("date").descending())
                );
        } else {
            if (strDate.isEmpty())
                meetPage = meetRepository.findByReceiversId(
                        authenticationService.getAuthenticatedUserId(),
                        PageRequest.of(page, size, Sort.by("date").descending())
                );
            else
                meetPage = meetRepository.findByReceiversIdAndDateBetween(
                        authenticationService.getAuthenticatedUserId(),
                        startOfDay, endOfDay,
                        PageRequest.of(page, size, Sort.by("date").descending())
                );
        }
        List<MeetResponse> meetResponses = meetPage.getContent().stream()
                .map(meetMapper::mapToMeetResponse).toList();
        return new PageResponse<>(
                meetResponses, page, size, meetPage.getTotalPages(), meetPage.getTotalElements()
        );
    }

}
