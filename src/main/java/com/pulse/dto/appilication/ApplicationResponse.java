package com.pulse.dto.appilication;

import com.pulse.dto.interview.JobInterviewResponse;
import com.pulse.dto.talent.TalentResponse;
import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ApplicationResponse {

    private Long id;
    private Long score;
    private Long experiencesScore;
    private Long projectsScore;
    private Long skillsScore;
    private boolean selected;
    private boolean approved;
    private boolean refused;

    private boolean hasAdminMeet;
    private boolean hasClientMeet;

    private TalentResponse talent;
    private Long jobId;

    private List<JobInterviewResponse> jobInterviews;
}
