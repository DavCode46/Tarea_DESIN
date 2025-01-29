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

	@Query("""
		   SELECT new com.davidmb.tarea3ADbase.dtos.StayView(
		        p.name,
		        p.nationality,
		        CASE WHEN s.id IS NOT NULL THEN true ELSE false END,
		        s.date,
		        s.vip
		    )
		    FROM Pilgrim p
		    LEFT JOIN p.pilgrimStops sp ON sp.pilgrim.id = p.id
		    LEFT JOIN Stay s ON s.pilgrim.id = p.id AND s.stop.id = sp.stop.id AND s.date = sp.stopDate
		    WHERE sp.stop.id = :stopId
		   
		    """)
		List<StayView> findAllStayViewsByStop(@Param("stopId") Long stopId);
	
	/**
	 *  SELECT p.nombre, 
       p.nacionalidad, 
       CASE WHEN s.id IS NOT NULL THEN true ELSE false END AS tiene_estancia,
       s.fecha, 
       s.vip
		FROM peregrinos p
		LEFT JOIN peregrinos_paradas pp ON pp.id_peregrino = p.id
		LEFT JOIN estancias s ON s.id_peregrino = p.id AND s.id_parada = pp.id_parada and s.fecha = pp.fecha_parada
		WHERE pp.id_parada = 1
		
		
		 SELECT new com.davidmb.tarea3ADbase.dtos.StayView(
		        p.name,



		        CASE WHEN s.id IS NOT NULL THEN true ELSE false END,
		        s.date,
		        s.vip
		    )
		    FROM Pilgrim p
		    LEFT JOIN p.pilgrimStops sp ON sp.pilgrim.id = p.id
		    LEFT JOIN Stay s ON s.pilgrim.id = p.id AND s.stop.id = sp.stop.id AND s.date = sp.stopDate
		    WHERE sp.stop.id = :stopId
	 * @param stopId
	 * @param startDate
	 * @param endDate
	 * @return
	 */




		@Query("""
		    SELECT new com.davidmb.tarea3ADbase.dtos.StayView(
		        p.name,
		        p.nationality,
		        CASE WHEN s.id IS NOT NULL THEN true ELSE false END,
		        s.date,
		        s.vip
		    )
		    FROM Pilgrim p
		    LEFT JOIN p.pilgrimStops sp ON sp.pilgrim.id = p.id
		    LEFT JOIN Stay s ON s.pilgrim.id = p.id AND s.stop.id = sp.stop.id AND s.date = sp.stopDate
		    WHERE sp.stop.id = :stopId AND s.date BETWEEN :startDate AND :endDate
		    """)
		List<StayView> findStayViewsByStopBetweenDates(
		    @Param("stopId") Long stopId,
		    @Param("startDate") LocalDate startDate,
		    @Param("endDate") LocalDate endDate
		);


	/**
	 
 SELECT p.nombre, 
       p.nacionalidad, 
       CASE WHEN s.id IS NOT NULL THEN true ELSE false END AS tiene_estancia,
       s.fecha, 
       s.vip
FROM peregrinos p
LEFT JOIN peregrinos_paradas pp ON pp.id_peregrino = p.id
LEFT JOIN estancias s ON s.id_peregrino = p.id AND s.id_parada = pp.id_parada and s.fecha = pp.fecha_parada
WHERE pp.id_parada = 1
	 * 
	 * @param id
	 * @return
	 */
	Pilgrim findByUserId(Long id);

}
