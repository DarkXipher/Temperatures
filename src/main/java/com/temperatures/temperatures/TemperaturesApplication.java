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
				"34C,24C,15F,98F,34F".split(","))
				.forEach(
						a -> {
							temperatureRepository.save(new Temperature(a, new Date(2018,04,01), new Date(2018,04,01)));
						});
	}
}
