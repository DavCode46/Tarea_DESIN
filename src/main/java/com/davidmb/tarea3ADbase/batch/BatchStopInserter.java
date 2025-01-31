package com.davidmb.tarea3ADbase.batch;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.repositories.StopRepository;

@Component
public class BatchStopInserter implements CommandLineRunner {

	private final StopRepository stopRepository;
	
	public BatchStopInserter(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
        }

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
