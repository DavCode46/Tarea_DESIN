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
		       + "JOIN p.pilgrimStops sp "
		       + "LEFT JOIN Stay s ON s.pilgrim.id = p.id AND s.stop.id = sp.stop.id "
		       + "WHERE sp.stop.id = :stopId")
		List<StayView> findAllStayViewsByStop(@Param("stopId") Long stopId);
	
	@Query("SELECT new com.davidmb.tarea3ADbase.dtos.StayView( "
		       + "p.name, "
		       + "p.nationality, "
		       + "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, "
		       + "s.date, "
		       + "s.vip) "
		       + "FROM Pilgrim p "
		       + "JOIN p.pilgrimStops sp "
		       + "LEFT JOIN Stay s ON s.pilgrim.id = p.id AND s.stop.id = sp.stop.id "
		       + "WHERE sp.stop.id = :stopId AND s.date BETWEEN :startDate AND :endDate")
		List<StayView> findStayViewsByStopBetweenDates(
				@Param("stopId")Long stopId, 
				@Param("startDate") LocalDate startDate, 
				@Param("endDate") LocalDate endDate);

	
	Pilgrim findByUserId(Long id);
	
}
