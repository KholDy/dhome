package com.github.kholdy.dhome.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kholdy.dhome.data.UserRepository;
import com.github.kholdy.dhome.model.Room;
import com.github.kholdy.dhome.model.User;
import com.github.kholdy.dhome.service.ClimateSensorService;
import com.github.kholdy.dhome.service.LightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.ResourceAccessException;

@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private LightService lightService;

	@Autowired
	private ClimateSensorService sensorService;

	private User user;

	private final Room LivingRoom;
	private final Room Bedroom;
	private final Room Hallway;
	private final Room Kitchen;

	@Autowired
	public HomeController(@Qualifier("Living room") Room LivingRoom,
						  @Qualifier("Bedroom") Room Bedroom,
						  @Qualifier("Hallway") Room Hallway,
						  @Qualifier("Kitchen") Room Kitchen) {

		this.LivingRoom = LivingRoom;
		this.Bedroom = Bedroom;
		this.Hallway = Hallway;
		this.Kitchen = Kitchen;
	}
	
	@ModelAttribute(name = "btnLivingRoom")
	public String addLivingRoom() {
		return "connection is fail";
	}
	
	@ModelAttribute(name = "btnBedroom")
	public String addBedroom() {
		return "connection is fail";
	}
	
	@ModelAttribute(name = "btnKitchen")
	public String addKitchen() {
		return "connection is fail";
	}
	
	@ModelAttribute(name = "btnHallway")
	public String addHallway() {
		return "connection is fail";
	}

	@ModelAttribute(name = "sensorTemperature")
	public String addSensorTemperature() {
		return "connection is fail";
	}

	@ModelAttribute(name = "sensorPressure")
	public String addSensorPressure() {
		return "connection is fail";
	}

	@ModelAttribute(name = "sensorHumidity")
	public String addSensorHumidity() {
		return "connection is fail";
	}
	
	@GetMapping()
	public String getStateLed(Model model) throws JsonProcessingException {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

		String username = loggedInUser.getName();
		user = userRepo.findByUsername(username).get();
		model.addAttribute("username", user.getUsername());

		// Проверяем состояние освещения(вкл/откл) во всех комнатах
		lightService.state(LivingRoom.getLight());
		lightService.state(Bedroom.getLight());
		lightService.state(Kitchen.getLight());
		lightService.state(Hallway.getLight());

		sensorService.getData(LivingRoom.getClimateSensor());

		// Меняем состояние на кнопке
		model.addAttribute(LivingRoom.getNameModelOfView(), LivingRoom.getName() + " light " + LivingRoom.getLight().getState());
		model.addAttribute(Bedroom.getNameModelOfView(), Bedroom.getName() + " light " + Bedroom.getLight().getState());
		model.addAttribute(Kitchen.getNameModelOfView(), Kitchen.getName() + " light " + Kitchen.getLight().getState());
		model.addAttribute(Hallway.getNameModelOfView(), Hallway.getName() + " light " + Hallway.getLight().getState());

		model.addAttribute("sensorTemperature", LivingRoom.getClimateSensor().getTemperature().substring(0, 4));
		model.addAttribute("sensorPressure", LivingRoom.getClimateSensor().getPressure().substring(0, 3));
		model.addAttribute("sensorHumidity", LivingRoom.getClimateSensor().getHumidity().substring(0, 4));
		return "home";
	}

	@PostMapping("/light-living-room")
	public String lightLivingRoom(Model model) throws Exception {
		// Включаем или отключаем свет
		lightService.selector(LivingRoom.getLight());
		// Получаем состояние освещения(вкл/откл) в гостинной.
		getStateLed(model);

		return "home";
	}
	
	@PostMapping("/light-bedroom")
	public String lightBedroom(Model model) throws Exception {
		// Включаем или отключаем свет
		lightService.selector(Bedroom.getLight());
		// Получаем состояние освещения(вкл/откл) в спальне.
		getStateLed(model);
		return "home";
	}
	
	@PostMapping("/light-kitchen")
	public String lightKitchen(Model model) {
		return "home";
	}
	
	@PostMapping("/light-hallway")
	public String lightHallway(Model model)  {
		return "home";
	}
}





























