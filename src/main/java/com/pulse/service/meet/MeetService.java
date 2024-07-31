package com.pulse.service.meet;

import com.pulse.dto.meet.MeetRequest;
import com.pulse.dto.meet.MeetResponse;
import com.pulse.dto.page.PageResponse;
import jakarta.mail.MessagingException;

public interface MeetService {
    MeetResponse addMeet(MeetRequest meetRequest) throws MessagingException;
    void closeMeet(Long id);
    void acceptMeet(Long id);

    PageResponse<MeetResponse> getMeets(String strDate, int page, int size);

    void refuseMeet(Long id);

   /*


    void refuseMeet(Long id);

   */
}
