package com.davidmb.tarea3ADbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Stop;


@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {

	Stop findByName(String name);
	
}
