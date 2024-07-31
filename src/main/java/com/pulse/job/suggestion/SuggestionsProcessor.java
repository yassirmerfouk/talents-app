package com.pulse.job.suggestion;

import com.pulse.dto.suggestion.SuggestedJob;
import com.pulse.dto.suggestion.Suggestion;
import com.pulse.enumeration.JobStatus;
import com.pulse.model.Job;
import com.pulse.model.Talent;
import com.pulse.repository.CustomJobRepository;
import com.pulse.repository.TalentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SuggestionsProcessor implements ItemProcessor<Talent, Suggestion> {

    private final CustomJobRepository customJobRepository;
    private final TalentRepository talentRepository;

    @Override
    public Suggestion process(@NonNull Talent talent) throws Exception {

        Set<Job> suggestedJobs = new HashSet<>();

        List<Job> todayJobs = customJobRepository.findByCreatedAtBetweenAndStatus(
                LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX),
                JobStatus.OPEN
        );

        todayJobs.forEach(job -> {
            job.getSkills().forEach(skill -> {
                if(talentRepository.hasSkill(talent.getId(), skill.getTitle()))
                    suggestedJobs.add(job);
            });
        });

        if(!suggestedJobs.isEmpty())
            return Suggestion.builder()
                    .firstName(talent.getFirstName())
                    .lastName(talent.getLastName())
                    .email(talent.getEmail())
                    .suggestedJobs(
                            suggestedJobs.stream().map(job -> SuggestedJob.builder()
                                    .id(job.getId())
                                    .title(job.getTitle())
                                    .jobType(job.getType())
                                    .contractType(job.getContractType())
                                    .shortDescription(job.getDescription().length() > 180 ? job.getDescription().substring(0, 180) : job.getDescription())
                                    .build()
                            ).collect(Collectors.toSet())
                    ).build();
        return null;
    }
}
