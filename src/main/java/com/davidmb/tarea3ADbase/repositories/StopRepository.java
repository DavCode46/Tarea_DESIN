package com.davidmb.tarea3ADbase.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Stop;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para la entidad `Stop`.
 * 
 * `Stop` representa una parada en el sistema de rutas de los peregrinos.
 * 
 * Extiende `JpaRepository`, proporcionando métodos para realizar operaciones CRUD sobre `Stop`.
 * 
 * @author DavidMB
 */
@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {

    /**
     * Busca una parada por su nombre.
     * 
     * @param name Nombre de la parada.
     * @return El objeto `Stop` correspondiente si existe, o `null` en caso contrario.
     */
	Stop findByName(String name);
	

    /**
     * Busca una parada asociada a un usuario específico.
     * 
     * @param id ID del usuario asociado a la parada.
     * @return El objeto `Stop` correspondiente si existe, o `null` en caso contrario.
     */
	Stop findByUserId(Long id);
	
    /**
     * Verifica si existe una parada con un nombre y región específicos.
     * 
     * @param name Nombre de la parada.
     * @param region Región en la que se encuentra la parada.
     * @return `true` si la parada existe, `false` en caso contrario.
     */
	boolean existsByNameAndRegion(String name, String region);
	
	
}
