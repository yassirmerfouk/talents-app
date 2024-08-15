package com.pulse.service.statistic;

import com.pulse.dto.statistic.StatisticResponse;
import com.pulse.repository.ClientRepository;
import com.pulse.repository.CustomJobRepository;
import com.pulse.repository.SelectionRepository;
import com.pulse.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{

    private final TalentRepository talentRepository;
    private final ClientRepository clientRepository;
    private final CustomJobRepository customJobRepository;
    private final SelectionRepository selectionRepository;

    @Override
    public StatisticResponse getStatistics(){

        return StatisticResponse.builder()
                .totalTalents(talentRepository.count())
                .totalEnabledTalents(talentRepository.countByEnabledTrue())
                .totalClients(clientRepository.count())
                .totalEnabledClients(clientRepository.countByEnabledTrue())
                .totalJobs(customJobRepository.count())
                .totalSelections(selectionRepository.count())
                .build();
    }
}
