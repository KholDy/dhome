package com.github.kholdy.dhome.config;

import com.github.kholdy.dhome.model.ClimateSensor;
import com.github.kholdy.dhome.model.Light;
import com.github.kholdy.dhome.model.Room;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "main")
public class LightConfig {

    @Bean(name = "Living room")
    @Scope("singleton")
    Room livingroom() {
        return new Room("Livingroom", "btnLivingRoom", addLightLivingRoom(), addClimateSensorAtHome());
    }

    @Bean(name = "Bedroom")
    @Scope("singleton")
    Room bedroom() {
        return new Room("Bedroom", "btnBedroom", addLightBedroom(), null);
    }

    @Bean(name = "Hallway")
    @Scope("singleton")
    Room hallway() {
        return new Room("Hallway","btnHallway", addLightHallway(), null);
    }

    @Bean(name = "Kitchen")
    @Scope("singleton")
    Room kitchen() {
        return new Room("Kitchen", "btnKitchen", addLightKitchen(), null);
    }

    @Bean
    @Scope("singleton")
    Light addLightLivingRoom() {
        return new Light(String.valueOf(1),
                "Main light in the living room ",
                "http://192.168.0.201",
                null,
                null);
    }

    @Bean
    @Scope("singleton")
    Light addLightBedroom() {
        return new Light(String.valueOf(2),
                "Main light in the bedroom",
                "http://192.168.0.202",
                null,
                null);
    }

    @Bean
    @Scope("singleton")
    Light addLightHallway() {
        return new Light(String.valueOf(3),
                "Main light hallway",
                "",
                null,
                null);
    }

    @Bean
    @Scope("singleton")
    Light addLightKitchen() {
        return new Light(String.valueOf(4),
                "Main light kitchen",
                "",
                null,
                null);
    }

    @Bean
    @Scope("singleton")
    ClimateSensor addClimateSensorAtHome() {
        return new ClimateSensor(String.valueOf(1),
                "http://192.168.0.210",
                "Sensor of temperature, pressure, humidity",
                null,
                null,
                null);
    }
}
