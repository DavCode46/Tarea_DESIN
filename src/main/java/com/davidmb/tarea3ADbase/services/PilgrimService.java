package com.davidmb.tarea3ADbase.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.repositories.PilgrimRepository;

@Service
public class PilgrimService {
;

	public PilgrimService( ) {
		
	}

	@Autowired
	private PilgrimRepository pilgrimRepository;

	@Transactional
	public Pilgrim save(Pilgrim entity) {
	    return pilgrimRepository.save(entity);
	}

	public Pilgrim update(Pilgrim entity) {
	   
	    return pilgrimRepository.save(entity);
	}


	public void delete(Pilgrim entity) {
		pilgrimRepository.delete(entity);
	}

	public void delete(Long id) {
		pilgrimRepository.deleteById(id);
	}


	public Pilgrim find(Long id) {
		return pilgrimRepository.findById(id).get();
	}
	
	public Pilgrim findByUserId(Long id) {
		return pilgrimRepository.findByUserId(id);
	}

	public List<Pilgrim> findAll() {
		return pilgrimRepository.findAll();
	}


	public void deleteInBatch(List<Pilgrim> pilgrims) {
		pilgrimRepository.deleteAll(pilgrims);
	}

}
