package com.davidmb.tarea3ADbase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Stay;


@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {

	List<Stay> findAllByPilgrim_Id(Long id);
	
}
