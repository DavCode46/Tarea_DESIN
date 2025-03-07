package com.davidmb.tarea3ADbase.batch;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.repositories.StopRepository;

/**
 * Clase encargada de insertar paradas por defecto en la base de datos al iniciar la aplicación.
 * 
 * Esta clase implementa {@link CommandLineRunner}, lo que permite ejecutar código automáticamente
 * al iniciar la aplicación Spring Boot. Se verifica si la base de datos ya contiene paradas antes
 * de insertar las paradas predefinidas.
 * 
 * Las paradas insertadas corresponden a localidades en España.
 * 
 * @author DavidMB
 */
@Component
public class BatchStopInserter implements CommandLineRunner {

	 /** Repositorio para la gestión de paradas en la base de datos */
	private final StopRepository stopRepository;
	
	 /**
     * Constructor de la clase que inyecta el repositorio de paradas.
     * 
     * @param stopRepository Repositorio de paradas.
     */
	public BatchStopInserter(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
        }

	 /**
     * Método que se ejecuta automáticamente al iniciar la aplicación.
     * 
     * Si la base de datos no contiene paradas, inserta un conjunto predefinido de paradas.
     * 
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     * @throws Exception Si ocurre un error durante la inserción de paradas.
     */
	@Override
	public void run(String... args) throws Exception {
		if (stopRepository.count() == 0) {
			List<Stop> stops = Arrays.asList(
					new Stop("Siero", "Esp", "Siero", 2L),
					new Stop("Oviedo", "Esp", "Oviedo", 3L),
					new Stop("Gijón", "Esp", "Gijón", 4L),
					new Stop("Nava", "Esp", "Nava", 5L));

			stopRepository.saveAll(stops);
		}
	}
}
