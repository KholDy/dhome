package com.github.kholdy.dhome.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Room {
    private final String name;
    private final String nameModelOfView;
    private final Light light;
}
