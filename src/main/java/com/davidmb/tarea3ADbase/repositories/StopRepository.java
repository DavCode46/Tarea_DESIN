package com.davidmb.tarea3ADbase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Stop;


@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {

	Stop findByName(String name);
	
	Stop findByUserId(Long id);
	
	 @Query("SELECT s FROM Stop s JOIN s.pilgrims p WHERE p.id = :pilgrimId")
	    List<Stop> findAllByPilgrimId(@Param("pilgrimId") Long pilgrimId);
	
}
