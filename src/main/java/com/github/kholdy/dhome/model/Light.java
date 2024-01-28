package com.github.kholdy.dhome.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Light {
    private String id;
    private String description;
    private String ip;
    private String state;
    private String action;

    @JsonCreator
    public Light(
            @JsonProperty("id") String id,
            @JsonProperty("description") String description,
            @JsonProperty("ip") String ip,
            @JsonProperty("state") String state,
            @JsonProperty("action") String action) {

        this.id = id;
        this.description = description;
        this.ip = ip;
        this.state = state;
        this.action = action;
    }
}
