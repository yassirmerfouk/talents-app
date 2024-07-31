package com.pulse.mapper;

import com.pulse.dto.meet.MeetRequest;
import com.pulse.dto.meet.MeetResponse;
import com.pulse.dto.user.UserResponse;
import com.pulse.model.Client;
import com.pulse.model.Meet;
import com.pulse.model.Talent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MeetMapper {

    public Meet mapToMeet(MeetRequest meetRequest){
        Meet meet = new Meet();
        BeanUtils.copyProperties(meetRequest, meet);
        return meet;
    }

    public MeetResponse mapToMeetResponse(Meet meet){
        MeetResponse meetResponse = new MeetResponse();
        BeanUtils.copyProperties(meet, meetResponse);
        meetResponse.setSender(
                UserResponse.builder().id(meet.getSender().getId())
                        .firstName(meet.getSender().getFirstName())
                        .lastName(meet.getSender().getLastName())
                        .type("ADMIN")
                        .build()
        );
        meetResponse.setReceivers(meet.getReceivers().stream().map(
                receiver -> UserResponse.builder().id(receiver.getId())
                        .firstName(receiver.getFirstName())
                        .lastName(receiver.getLastName())
                        .type(
                                receiver instanceof Client ?
                                        "CLIENT" :
                                        receiver instanceof Talent ? "TALENT" : "ADMIN"
                        ).build()
        ).toList());
        return meetResponse;
    }
}
