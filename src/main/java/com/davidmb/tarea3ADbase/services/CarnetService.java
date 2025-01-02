package com.davidmb.tarea3ADbase.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.models.Carnet;
import com.davidmb.tarea3ADbase.repositories.CarnetRepository;

@Service
public class CarnetService {

	

	public CarnetService() {
		
	}

	@Autowired
	private CarnetRepository carnetRepository;

	public Carnet save(Carnet entity) {
	   
	    return carnetRepository.save(entity);
	}

	public Carnet update(Carnet entity) {
	   
	    return carnetRepository.save(entity);
	}


	public void delete(Carnet entity) {
		carnetRepository.delete(entity);
	}

	public void delete(Long id) {
		carnetRepository.deleteById(id);
	}



	public Carnet find(Long id) {
		return carnetRepository.findById(id).get();
	}

	public List<Carnet> findAll() {
		return carnetRepository.findAll();
	}


	public void deleteInBatch(List<Carnet> carnets) {
		carnetRepository.deleteAll(carnets);
	}

}
