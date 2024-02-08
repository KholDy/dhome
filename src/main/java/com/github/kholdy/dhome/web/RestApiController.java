package com.github.kholdy.dhome.web;

import com.github.kholdy.dhome.model.Light;
import com.github.kholdy.dhome.model.Room;
import com.github.kholdy.dhome.service.ClimateSensorService;
import com.github.kholdy.dhome.service.LightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestApiController {
    private final List<Room> listOfRooms = new ArrayList<>();

    @Autowired
    private LightService lightService;

    @Autowired
    private ClimateSensorService sensorService;

    private final Room LivingRoom;
    private final Room Bedroom;
    private final Room Hallway;
    private final Room Kitchen;

    public RestApiController(@Qualifier("living room") Room LivingRoom,
                             @Qualifier("bedroom") Room Bedroom,
                             @Qualifier("hallway") Room Hallway,
                             @Qualifier("kitchen") Room Kitchen) {
        this.LivingRoom = LivingRoom;
        this.Bedroom = Bedroom;
        this.Hallway = Hallway;
        this.Kitchen = Kitchen;

        listOfRooms.add(this.LivingRoom);
        listOfRooms.add(this.Bedroom);
        listOfRooms.add(this.Hallway);
        listOfRooms.add(this.Kitchen);
    }

    @GetMapping("/rooms")
    Iterable<Room> getRooms() throws Exception {
        for (Room r : listOfRooms) {
            if(r.getLight() != null) lightService.state(r.getLight());
            if(r.getClimateSensor() != null) sensorService.getData(r.getClimateSensor());
        }
        return listOfRooms;
    }

    @GetMapping("/{name}")
    Optional<Room> getLightByName(@PathVariable String name) {
        for (Room r: listOfRooms) {
            if (r.getName().equals(name)) return Optional.of(r);
        }

        return Optional.empty();
    }

    @PostMapping("/{name}/{idLight}")
    Light putLight(@PathVariable String name, @PathVariable String idLight, @RequestBody Light light) throws Exception {
        for (Room r: listOfRooms) {
            if (r.getName().equals(name)) {
                if (r.getLight().getId().equals(light.getId())) {
                    r.getLight().setAction(light.getAction());
                    return lightService.sendRequestLightRest(r.getLight());
                }
            }
        }
        return light;
    }
}
