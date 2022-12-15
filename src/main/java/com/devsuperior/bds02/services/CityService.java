package com.devsuperior.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.services.exception.DatabaseException;
import com.devsuperior.bds02.services.exception.ResourceNotFoundException;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;

	@Autowired
	private ModelMapper modelMapper;

	
	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		List<City> list = repository.findAll(Sort.by("name"));
		return list.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
	}

 	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Error. Id not found: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Error. Integrity violation: " + id);
		}

	}

	@Transactional
	public CityDTO insert(CityDTO cityDTO) {
		City entity = new City();
		entity = modelMapper.map(cityDTO, City.class);
		entity = repository.save(entity); // reposity.save() returns a reference to object saved in DB
		return modelMapper.map(entity, CityDTO.class);
	}
}