package com.temperatures.temperatures;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TemperatureRepository extends JpaRepository<Temperature, Long>
{
	Optional<Temperature> findTemperatureById(Long id);
	void deleteById(Long id);
}
