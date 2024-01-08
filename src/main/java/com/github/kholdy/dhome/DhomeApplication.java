package com.github.kholdy.dhome;

import com.github.kholdy.dhome.config.LightConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class DhomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DhomeApplication.class, args);
		ApplicationContext context = new AnnotationConfigApplicationContext(LightConfig.class);
	}

}
