package com.temperatures.temperatures;

import java.sql.Date;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TemperaturesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemperaturesApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(TemperatureRepository temperatureRepository) {
		return (evt) -> Arrays.asList(
				"34,24,15,98,34".split(","))
				.forEach(
						a -> {
							temperatureRepository.save(new Temperature(a, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
							temperatureRepository.save(new Temperature(a, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), "Farenheit"));
							temperatureRepository.save(new Temperature(a, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), "Kelvin"));
						});
	}
}
