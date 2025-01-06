package com.davidmb.tarea3ADbase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.Pilgrim;


@Repository
public interface PilgrimRepository extends JpaRepository<Pilgrim, Long> {
	
	@Query("SELECT new com.davidmb.tarea3ADbase.dtos.StayView( "
	         + "p.id, p.name, p.nationality, s.date, s.vip, sp.id) "
	         + "FROM Pilgrim p "
	         + "JOIN p.stays s "
	         + "JOIN p.stops sp")
	    List<StayView> findAllStayViews();

	Pilgrim findByUserId(Long id);
	
}
