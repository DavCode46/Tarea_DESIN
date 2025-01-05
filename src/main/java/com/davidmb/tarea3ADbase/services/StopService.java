package com.davidmb.tarea3ADbase.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.repositories.StopRepository;

@Service
public class StopService {

	public StopService() {

	}

	@Autowired
	private StopRepository stopRepository;

	public Stop save(Stop entity) {

		return stopRepository.save(entity);
	}

	public Stop update(Stop entity) {
		return stopRepository.save(entity);
	}

	public void delete(Stop entity) {
		stopRepository.delete(entity);
	}

	public void delete(Long id) {
		stopRepository.deleteById(id);
	}

	public Stop find(Long id) {
		return stopRepository.findById(id).get();
	}

	public List<Stop> findAll() {
		return stopRepository.findAll();
	}
	
	public List<Stop> findAllByPilgrimId(Long id) {
		return stopRepository.findAllByPilgrimId(id);
	}
	
	public Stop findByName(String name) {
		return stopRepository.findByName(name);
	}

	public void deleteInBatch(List<Stop> users) {
		stopRepository.deleteAll(users);
	}

}
