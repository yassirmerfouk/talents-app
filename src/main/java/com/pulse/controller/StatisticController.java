package com.pulse.controller;

import com.pulse.dto.statistic.StatisticResponse;
import com.pulse.service.statistic.StatisticService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StatisticResponse> getStatistics(){
        return new ResponseEntity<>(
                statisticService.getStatistics(),
                HttpStatus.OK
        );
    }
}
