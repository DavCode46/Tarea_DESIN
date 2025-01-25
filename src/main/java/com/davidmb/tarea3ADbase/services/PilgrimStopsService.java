package com.davidmb.tarea3ADbase.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.PilgrimStops;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.repositories.PilgrimStopsRepository;

@Service
public class PilgrimStopsService {

	@Autowired
	private PilgrimStopsRepository pilgrimStopsRepository;
	
	public boolean existsByPilgrimAndStopAndStopDate(Pilgrim pilgrim, Stop stop, LocalDate stopDate) {
		return pilgrimStopsRepository.existsByPilgrimAndStopAndStopDate(pilgrim, stop, stopDate);
	}
	
	public PilgrimStops save(PilgrimStops entity) {
		return pilgrimStopsRepository.save(entity);
	}

}
