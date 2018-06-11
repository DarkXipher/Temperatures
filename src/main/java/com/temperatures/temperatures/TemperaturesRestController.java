package com.temperatures.temperatures;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/")
public class TemperaturesRestController
{
	private final TemperatureRepository temperatureRepository;

	@Autowired
	public TemperaturesRestController(TemperatureRepository temperatureRepository) {
		this.temperatureRepository = temperatureRepository;
	}

	/**
	 * Method to return all temperatures.
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<Temperature> readTemperatures() {
		return this.temperatureRepository.findAll();
	}

	/**
	 * Method to retrieve temperatures to add by post
	 * @param unused
	 * @param input
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@RequestBody Temperature input) {
		/*
		if(this.temperatureRepository.exists(input.getId()))
		{
			this.temperatureRepository.saveAndFlush(entity)
		} else {
			
		}*/
		Date created, updated;
		if(input.getCreateDate() == null)
		{
			created = new Date(System.currentTimeMillis());
		} else {
			created = input.getCreateDate();
		}
		if(input.getUpdateDate() == null)
		{
			updated = new Date(System.currentTimeMillis());
		} else {
			updated = input.getUpdateDate();
		}
		
		Temperature temp = this.temperatureRepository.saveAndFlush(new Temperature(input.getTemperature(), created, updated));
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(temp);
		//return this.temperatureRepository.save(input);
				/*
				findTemperatureById(input.getId())
			.map(temperature -> {
					Temperature Temp  = this.temperatureRepository.save( new Temperature(input.getTemperature(), temperature.getCreateDate(), input.getUpdateDate()));
					
					return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri()).build();
			})
			.orElse(ResponseEntity.noContent().build()) ;
		*/
		/*return this.accountRepository
				.findByUsername(userId)
				.map(temperature -> {
					Bookmark result = bookmarkRepository.save(new Bookmark(account,
							input.getUri(), input.getDescription()));

					URI location = ServletUriComponentsBuilder
						.fromCurrentRequest().path("/{id}")
						.buildAndExpand(result.getId()).toUri();

					return ResponseEntity.created(location).build();
				})
				.orElse(ResponseEntity.noContent().build());
		 */
	}

	/**
	 * Method to HTTP put data into our data collection. 
	 * 
	 * e.g. PUT http://example.com/order/1
	 */
	@RequestMapping(method = RequestMethod.PUT)
	ResponseEntity<?> put(@PathVariable Long id, @RequestBody Temperature input)
	{
		return this.temperatureRepository.findTemperatureById(id)
				.map(temperature -> {
						Temperature temp  = this.temperatureRepository.save( new Temperature(input.getTemperature(), temperature.getCreateDate(), input.getUpdateDate()));
						
						return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(temp);
				})
				.orElse(ResponseEntity.noContent().build()) ;	}
	
	/**
	 * Method to HTTP Delete data from our collection based off of the ID from the request.
	 * 
	 * e.g. DELETE http://example.com/order/1
	 * @param inputId The id of the temperature to delete
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	void delete(@PathVariable Long inputId)
	{
		this.temperatureRepository.delete(inputId);
	}
}
