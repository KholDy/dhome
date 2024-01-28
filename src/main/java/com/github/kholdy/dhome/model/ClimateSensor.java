package com.github.kholdy.dhome.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClimateSensor {
    private String id;
    private String ip;
    private String description;
    private String temperature;
    private String pressure;
    private String humidity;

    @JsonCreator
    public ClimateSensor(
            @JsonProperty("id") String id,
            @JsonProperty("ip") String ip,
            @JsonProperty("description") String description,
            @JsonProperty("temperature") String temperature,
            @JsonProperty("pressure") String pressure,
            @JsonProperty("humidity") String humidity) {
        this.id = id;
        this.ip = ip;
        this.description = description;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }
}
