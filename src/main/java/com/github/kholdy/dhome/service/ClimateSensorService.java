package com.github.kholdy.dhome.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kholdy.dhome.model.ClimateSensor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class ClimateSensorService {

    public ClimateSensor getData(ClimateSensor sensor) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        URI url = URI.create(sensor.getIp() + "/data");
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ClimateSensor responseSensor = mapper.readValue(response.getBody(), ClimateSensor.class);

        sensor.setTemperature(responseSensor.getTemperature());
        sensor.setPressure(responseSensor.getPressure());
        sensor.setHumidity(responseSensor.getHumidity());

        return sensor;
    }
}
