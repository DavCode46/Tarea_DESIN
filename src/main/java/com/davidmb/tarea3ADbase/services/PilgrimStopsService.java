package com.davidmb.tarea3ADbase.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.repositories.PilgrimStopsRepository;

@Service
public class PilgrimStopsService {

	@Autowired
	private PilgrimStopsRepository pilgrimStopsRepository;
	
	public boolean existsByPilgrimAndStopAndStopDate(Long pilgrimId, Long stopId, LocalDate stopDate) {
		return pilgrimStopsRepository.existsByPilgrim_IdAndStop_IdAndStopDate(pilgrimId, stopId, stopDate);
	}

}
