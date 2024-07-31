package com.pulse.service.location;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService{

    @Value("${cities.file}")
    private String citiesFile;

    public List<Map<String, Object>> getFullCities() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(citiesFile), new TypeReference<List<Map<String, Object>>>() {});
    }

    @Override
    public List<String> getCities(){
        try{
            return getFullCities().stream().map(fullCity -> fullCity.get("city").toString()).collect(Collectors.toList());
        }catch (IOException e){
            throw new CustomException(e.getMessage());
        }
    }
}
