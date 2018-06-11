package com.temperatures.temperatures;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TemperatureNotFoundException extends RuntimeException 
{
	private static final long serialVersionUID = -2529054947024253633L;

	public TemperatureNotFoundException(String tempId) {
		super("Could not find temperature ID '" +tempId+ "'.");
	}
}
