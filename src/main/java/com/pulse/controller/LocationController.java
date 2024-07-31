package com.pulse.controller;

import com.pulse.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities(){
        return new ResponseEntity<>(
                locationService.getCities(),
                HttpStatus.OK
        );
    }
}
