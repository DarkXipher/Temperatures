package com.temperatures.temperatures;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<Temperature> readTemperatures() {
		return this.temperatureRepository.findAll();
	}

	/**
	 * Method to create new temperatures to add to our data collection. This method will take incoming data from
	 * the body and input it into our collection. If either of the date fields are empty, the system will fill in the
	 * data itself. Note, using POST will duplicate any records, and will not overwrite any data. This is the significance
	 * of POST vs PUT.
	 * 
	 * @param input incoming JSON data representing the Temperature.
	 * @return status 201 created when the data has been entered. Will also include the added data in the response body
	 * 			as the date fields may need to be reported back.
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@RequestBody Temperature input) {
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
		
		Temperature temp = this.temperatureRepository.saveAndFlush(new Temperature(input.getTemperature(), created, updated, input.getScale()));
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(temp);

	}

	/**
	 * Method to HTTP put data into our data collection. The intent of this method is to have a replace method 
	 * as opposed to the POST above. Using PUT will require an id and replace the contents of
	 * the given ID with the incoming body of the message. If the id is not found, a Not Found (404) is returned,
	 * otherwise a status 200 should be returned.
	 * 
	 * e.g. PUT http://example.com/order/1
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	ResponseEntity<?> put(@PathVariable String id, @RequestBody Temperature input)
	{
		Long lid = new Long(id);
		Date updated;
		if(input.getUpdateDate() == null)
		{
			updated = new Date(System.currentTimeMillis());
		} else {
			updated = input.getUpdateDate();
		}
		return this.temperatureRepository.findTemperatureById(lid)
				.map(temperature -> {
						Temperature temp  = this.temperatureRepository.save( new Temperature(lid, input.getTemperature(), temperature.getCreateDate(), updated, input.getScale()));
						
						return ResponseEntity.ok(temp);
				})
				.orElseThrow( () -> new TemperatureNotFoundException(id));
		//ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(this.temperatureRepository.save(new Temperature(lid, input.getTemperature(), new Date(System.currentTimeMillis()), updated, input.getScale()))));	
	}
	
	/**
	 * Method to HTTP Delete data from our collection based off of the ID from the request.
	 * 
	 * e.g. DELETE http://example.com/order/1
	 * @param inputId The id of the temperature to delete
	 */
	@RequestMapping(value = "/{inputId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable String inputId)
	{
		Long lid = new Long(inputId);
		try {
			this.temperatureRepository.delete(lid);
			return ResponseEntity.status(204).build();
		} catch( EmptyResultDataAccessException erdae)
		{
			throw new TemperatureNotFoundException(inputId);
		}
	}
}
