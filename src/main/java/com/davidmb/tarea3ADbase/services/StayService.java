package com.davidmb.tarea3ADbase.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.models.Stay;
import com.davidmb.tarea3ADbase.repositories.StayRepository;

@Service
public class StayService {

	public StayService() {

	}
	// BETWEEN PARA COGER ENTRE DOS FECHAS
	@Autowired
	private StayRepository stayRepository;

	public Stay save(Stay entity) {
		return stayRepository.save(entity);
	}

	public Stay update(Stay entity) {
		return stayRepository.save(entity);
	}

	public void delete(Stay entity) {
		stayRepository.delete(entity);
	}

	public void delete(Long id) {
		stayRepository.deleteById(id);
	}

	public Stay find(Long id) {
		return stayRepository.findById(id).get();
	}
	
	public Stay findByPilgrimIdAndStopIdAndDate(Long pilgrimId, Long stopId, LocalDate date) {
        return stayRepository.findByPilgrim_IdAndStop_IdAndDate(pilgrimId, stopId, date);
    }

	public List<Stay> findAllByPilgrimId(Long id) {
		return stayRepository.findAllByPilgrim_Id(id);
	}

	public List<Stay> findAll() {
		return stayRepository.findAll();
	}

	public void deleteInBatch(List<Stay> users) {
		stayRepository.deleteAll(users);
	}

}
