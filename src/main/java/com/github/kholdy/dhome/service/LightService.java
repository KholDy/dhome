package com.github.kholdy.dhome.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kholdy.dhome.model.Light;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class LightService {
	private final String RESPONSE_ON = "{\"state\":\"on\"}";
	private final String RESPONSE_OFF = "{\"state\":\"off\"}";

	// Вкл/Откл света
	public Light selector(Light light) throws Exception {
		if(light.getState().equals("on")) {
			light.setAction("off");
        } else {
			light.setAction("on");
        }
        return sendRequestLightRest(light);
	}

	// Получение состояния вкл/откл света
	public Light state(Light light) throws Exception {
		URI url = URI.create(light.getIp() + "/ledState");
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		if(response.getBody().equals(RESPONSE_ON)) {
			light.setState("on");
		} else if(response.getBody().equals(RESPONSE_OFF)) {
			light.setState("off");
		}

		return light;
	}

	// Отправка запроса на смену состояни освещения(вкл/откл)
	public Light sendRequestLightRest(Light light) throws Exception {
		URI url = URI.create(light.getIp() + "/led");

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(light);

		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

		ResponseEntity<String> lightResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		Light lightEsp = mapper.readValue(lightResponse.getBody(), Light.class);

		if(lightEsp.getState().equals("on")) {
			light.setState(lightEsp.getState());
			light.setAction(lightEsp.getAction());
		} else if(lightEsp.getState().equals("off")) {
			light.setState(lightEsp.getState());
			light.setAction(lightEsp.getAction());
		}

		return light;
	}
}









