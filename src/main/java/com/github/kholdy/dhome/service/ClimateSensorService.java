package com.github.kholdy.dhome.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kholdy.dhome.model.ClimateSensor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;

@Service
public class ClimateSensorService {

    @Value("${rest.template.timeout}")
    private int restTemplateTimeoutMs;

    public ClimateSensor getData(ClimateSensor sensor) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        URI url = URI.create(sensor.getIp() + "/data");
        RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(restTemplateTimeoutMs))
                .setReadTimeout(Duration.ofMillis(restTemplateTimeoutMs)).build();


        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ClimateSensor responseSensor = mapper.readValue(response.getBody(), ClimateSensor.class);

        sensor.setTemperature(responseSensor.getTemperature());
        sensor.setPressure(responseSensor.getPressure());
        sensor.setHumidity(responseSensor.getHumidity());

        return sensor;
    }
}
