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

    @Bean(name = "living room")
    @Scope("singleton")
    Room livingroom() {
        return new Room("livingroom", "btnLivingRoom", addLightLivingRoom(), addClimateSensorAtHome());
    }

    @Bean(name = "bedroom")
    @Scope("singleton")
    Room bedroom() {
        return new Room("bedroom", "btnBedroom", addLightBedroom(), null);
    }

    @Bean(name = "hallway")
    @Scope("singleton")
    Room hallway() {
        return new Room("hallway","btnHallway", addLightHallway(), null);
    }

    @Bean(name = "kitchen")
    @Scope("singleton")
    Room kitchen() {
        return new Room("kitchen", "btnKitchen", addLightKitchen(), null);
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
                "http://192.168.0.203",
                null,
                null);
    }

    @Bean
    @Scope("singleton")
    Light addLightKitchen() {
        return new Light(String.valueOf(4),
                "Main light kitchen",
                "http://192.168.0.204",
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
