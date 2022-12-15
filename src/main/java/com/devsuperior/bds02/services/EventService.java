package com.devsuperior.bds02.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exception.ResourceNotFoundException;
 
@Service
@Transactional(readOnly = true)
public class EventService {

	@Autowired
	private EventRepository repository;

	@Autowired
	private ModelMapper modelMapper;

 
	public Page<EventDTO> findAllPaged(Pageable pageRequest) {
		Page<Event> list = repository.findAll(pageRequest);
		// Page already is an stream since Java 8.X, noo need to convert
		return list.map(p -> modelMapper.map(p, EventDTO.class));

	}
	
	@Transactional
	public EventDTO update(Long id, EventDTO productDTO) {

			Optional<Event> obj = repository.findById(id);
			Event entity = obj.orElseThrow(() -> new ResourceNotFoundException("Error. Id not found: " + id));
			
			entity = modelMapper.map(productDTO, Event.class);
			entity.setId(id);
							
			entity = repository.save(entity);
			return modelMapper.map(entity, EventDTO.class);
 
	}
}