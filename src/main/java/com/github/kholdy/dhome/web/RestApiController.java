package com.github.kholdy.dhome.web;

import com.github.kholdy.dhome.model.Light;
import com.github.kholdy.dhome.model.Room;
import com.github.kholdy.dhome.service.LightService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RestApiController {
    private final List<Room> listOfRooms = new ArrayList<>();
    private final LightService lightService;

    private final Room LivingRoom;
    private final Room Bedroom;
    private final Room Hallway;
    private final Room Kitchen;

    public RestApiController(@Qualifier("Living room") Room LivingRoom,
                             @Qualifier("Bedroom") Room Bedroom,
                             @Qualifier("Hallway") Room Hallway,
                             @Qualifier("Kitchen") Room Kitchen) {
        this.LivingRoom = LivingRoom;
        this.Bedroom = Bedroom;
        this.Hallway = Hallway;
        this.Kitchen = Kitchen;

        this.lightService = new LightService();

        listOfRooms.add(this.LivingRoom);
        listOfRooms.add(this.Bedroom);
        //listOfRooms.add(this.Hallway);
        //listOfRooms.add(this.Kitchen);
    }

    @GetMapping
    Iterable<Room> getRooms() throws Exception {
        for (Room r : listOfRooms) {
            lightService.state(r.getLight());
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
