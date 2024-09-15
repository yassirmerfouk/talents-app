package com.pulse.service.meet;

import com.pulse.dto.meet.MeetRequest;
import com.pulse.dto.meet.MeetResponse;
import com.pulse.dto.page.PageResponse;
import jakarta.mail.MessagingException;

public interface MeetService {
    MeetResponse addMeet(MeetRequest meetRequest) throws MessagingException;

    MeetResponse addSelectionMeet(MeetRequest meetRequest);

    void closeMeet(Long id);
    PageResponse<MeetResponse> getMeets(String strDate, int page, int size);

}
