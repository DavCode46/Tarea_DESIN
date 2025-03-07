package com.davidmb.tarea3ADbase.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Stay;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para la entidad `Stay`.
 * 
 * `Stay` representa la estancia de un peregrino en una parada en una fecha determinada.
 * 
 * Extiende `JpaRepository`, proporcionando métodos para realizar operaciones CRUD sobre `Stay`.
 * 
 * @author DavidMB
 */
@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {

    /**
     * Recupera todas las estancias asociadas a un peregrino específico.
     * 
     * @param id ID del peregrino.
     * @return Lista de objetos `Stay` que representan todas las estancias del peregrino.
     */
	List<Stay> findAllByPilgrim_Id(Long id);
	
    /**
     * Verifica si existe una estancia registrada para un peregrino en una parada y fecha específicas.
     * 
     * @param pilgrimId ID del peregrino.
     * @param stopId ID de la parada.
     * @param date Fecha de la estancia.
     * @return `true` si existe una estancia con estos datos, `false` en caso contrario.
     */
	boolean existsByPilgrim_IdAndStop_IdAndDate(Long pilgrimId, Long stopId, LocalDate date);
	
    /**
     * Busca una estancia específica de un peregrino en una parada y fecha determinadas.
     * 
     * @param pilgrimId ID del peregrino.
     * @param stopId ID de la parada.
     * @param date Fecha de la estancia.
     * @return El objeto `Stay` correspondiente si existe, o `null` en caso contrario.
     */
	Stay findByPilgrim_IdAndStop_IdAndDate(Long pilgrimId, Long stopId, LocalDate date);
	
}
