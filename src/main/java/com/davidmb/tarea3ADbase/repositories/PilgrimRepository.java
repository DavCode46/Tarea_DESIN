package com.davidmb.tarea3ADbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Pilgrim;


@Repository
public interface PilgrimRepository extends JpaRepository<Pilgrim, Long> {

	Pilgrim findByUserId(Long id);
	
}
