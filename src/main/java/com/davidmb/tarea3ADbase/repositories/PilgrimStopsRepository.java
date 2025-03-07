package com.davidmb.tarea3ADbase.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.PilgrimStops;
import com.davidmb.tarea3ADbase.models.Stop;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para la entidad `PilgrimStops`.
 * 
 * Esta entidad representa la relación entre un peregrino (`Pilgrim`) y las paradas (`Stop`) que ha visitado.
 * 
 * Extiende `JpaRepository`, proporcionando métodos para realizar operaciones CRUD sobre `PilgrimStops`.
 * 
 * @author DavidMB
 */
@Repository
public interface PilgrimStopsRepository extends JpaRepository<PilgrimStops, Long> {
	
    /**
     * Verifica si existe un registro en la base de datos donde un peregrino ha visitado
     * una parada en una fecha específica.
     * 
     * @param pilgrim   Peregrino que se desea verificar.
     * @param stop      Parada que se desea verificar.
     * @param stopDate  Fecha de la parada a verificar.
     * @return `true` si existe un registro con estos datos, `false` en caso contrario.
     */
	boolean existsByPilgrimAndStopAndStopDate(Pilgrim pilgrim, Stop stop, LocalDate stopDate);
	
    /**
     * Recupera todas las paradas visitadas por un peregrino en función de su ID.
     * 
     * @param id ID del peregrino.
     * @return Lista de objetos `PilgrimStops` que representan las paradas visitadas por el peregrino.
     */
	 List<PilgrimStops> findAllByPilgrim_Id(Long id);
	 
	
}
