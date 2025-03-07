package com.davidmb.tarea3ADbase.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.Pilgrim;


/**
 * Repositorio que gestiona las operaciones de acceso a datos de la entidad `Pilgrim` (Peregrino).
 * 
 * Extiende `JpaRepository`, proporcionando métodos para realizar operaciones CRUD 
 * y consultas personalizadas sobre peregrinos en la base de datos.
 * 
 * @author DavidMB
 */
@Repository
public interface PilgrimRepository extends JpaRepository<Pilgrim, Long> {

    /**
     * Obtiene una lista de vistas (`StayView`) con información sobre las estancias de los peregrinos 
     * en una parada específica.
     * 
     * La consulta devuelve:
     * - Nombre del peregrino.
     * - Nacionalidad del peregrino.
     * - Fecha de la parada.
     * - Si el peregrino se alojó en la parada (valor booleano).
     * - Fecha de la estancia (si aplicable).
     * - Si la estancia fue VIP (si aplicable).
     * 
     * @param stopId ID de la parada de la cual se desean obtener las estancias.
     * @return Lista de objetos `StayView` con la información de los peregrinos en la parada.
     */
	@Query("""
		   SELECT new com.davidmb.tarea3ADbase.dtos.StayView(
		        p.name,
		        p.nationality,
		        sp.stopDate,
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
     * Obtiene una lista de vistas (`StayView`) con información sobre las estancias de los peregrinos 
     * en una parada específica dentro de un rango de fechas determinado.
     * 
     * @param stopId ID de la parada.
     * @param startDate Fecha de inicio del rango.
     * @param endDate Fecha de fin del rango.
     * @return Lista de objetos `StayView` con la información de los peregrinos en la parada dentro del rango de fechas.
     */
		@Query("""
		    SELECT new com.davidmb.tarea3ADbase.dtos.StayView(
		        p.name,
		        p.nationality,
		        sp.stopDate,
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
	     * Busca un peregrino en la base de datos a partir del ID de usuario.
	     * 
	     * @param id ID del usuario asociado al peregrino.
	     * @return El objeto `Pilgrim` correspondiente al usuario, o `null` si no existe.
	     */
	Pilgrim findByUserId(Long id);

}
