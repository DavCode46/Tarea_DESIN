package com.davidmb.tarea3ADbase.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.models.SendHome;
import com.davidmb.tarea3ADbase.repositories.SendHomeRepository;

@Service
public class SendHomeService {
	@Autowired
	SendHomeRepository sendHomeRepository;

	public void save(SendHome sendHome) {
		sendHomeRepository.save(sendHome);
	}
	
	public List<SendHome> getAllByStopId(Long stopId) {
		return sendHomeRepository.getAllByStopId(stopId);
	}
	
	public SendHome findById(Long id) {
		return sendHomeRepository.findById(id);
	}
}
