package com.davidmb.tarea3ADbase.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.Pilgrim;


@Repository
public interface PilgrimRepository extends JpaRepository<Pilgrim, Long> {
	
	@Query("SELECT new com.davidmb.tarea3ADbase.dtos.StayView( "
		       + "p.name, "
		       + "p.nationality, "
		       + "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, "
		       + "s.date, "
		       + "s.vip) "
		       + "FROM Pilgrim p "
		       + "JOIN p.stops sp "
		       + "LEFT JOIN Stay s ON s.pilgrim.id = p.id AND s.stop.id = sp.id "
		       + "WHERE sp.id = :stopId")
		List<StayView> findAllStayViewsByStop(@Param("stopId") Long stopId);

	
	Pilgrim findByUserId(Long id);
	
}
