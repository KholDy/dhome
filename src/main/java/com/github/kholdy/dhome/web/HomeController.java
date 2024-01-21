package com.github.kholdy.dhome.web;

import com.github.kholdy.dhome.data.UserRepository;
import com.github.kholdy.dhome.model.Room;
import com.github.kholdy.dhome.model.User;
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

@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private LightService lightService;

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
	
	@GetMapping()
	public String getStateLed(Model model){
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

		String username = loggedInUser.getName();
		user = userRepo.findByUsername(username).get();
		model.addAttribute("username", user.getUsername());

		// Проверяем состояние освещения(вкл/откл) во всех комнатах

		lightService.state(LivingRoom.getLight());
		lightService.state(Bedroom.getLight());
		//lightService.state(Kitchen.getLight());
		//lightService.state(Hallway.getLight());

		// Меняем состояние на кнопке
		model.addAttribute(LivingRoom.getNameModelOfView(), LivingRoom.getName() + " light " + LivingRoom.getLight().getState());
		model.addAttribute(Bedroom.getNameModelOfView(), Bedroom.getName() + " light " + Bedroom.getLight().getState());
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





























