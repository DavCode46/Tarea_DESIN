package com.davidmb.tarea3ADbase.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.davidmb.tarea3ADbase.dtos.ServiceResponse;
import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.Stay;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.repositories.PilgrimRepository;
import com.davidmb.tarea3ADbase.repositories.StayRepository;

import jakarta.persistence.EntityManager;

@Service
public class PilgrimService {

	@Autowired
	private StopService stopService;

	@Autowired
	private PilgrimRepository pilgrimRepository;
	
	@Autowired
	private StayRepository stayRepository;

	@Autowired
	private EntityManager entityManager;

	public PilgrimService() {

	}

	@Transactional
	public Pilgrim save(Pilgrim entity) {
		return pilgrimRepository.save(entity);
	}

	public Pilgrim update(Pilgrim entity) {

		return pilgrimRepository.save(entity);
	}

	public void delete(Pilgrim entity) {
		pilgrimRepository.delete(entity);
	}

	public void delete(Long id) {
		pilgrimRepository.deleteById(id);
	}

	public Pilgrim find(Long id) {
		return pilgrimRepository.findById(id).get();
	}

	public Pilgrim findByUserId(Long id) {
		return pilgrimRepository.findByUserId(id);
	}

	public List<Pilgrim> findAll() {
		return pilgrimRepository.findAll();
	}

	public List<StayView> findAllStayViewsByStop(Long stopId) {
		return pilgrimRepository.findAllStayViewsByStop(stopId);
	}

	public void deleteInBatch(List<Pilgrim> pilgrims) {
		pilgrimRepository.deleteAll(pilgrims);
	}

	@Transactional
	public ServiceResponse<Pilgrim> stampCard(Pilgrim pilgrim, Stop stop, boolean isVip, boolean createStay) {
		boolean stampStop = false;
		boolean stampStay = false;
		// Recuperar datos actualizados desde la base de datos
		Pilgrim dbPilgrim = pilgrimRepository.findById(pilgrim.getId())
				.orElseThrow(() -> new IllegalArgumentException("Peregrino no encontrado"));
		
		Stop dbStop = stopService.find(stop.getId());

		// Asociar la parada al peregrino
		if (!dbPilgrim.getStops().contains(dbStop)) {
			dbPilgrim.getStops().add(dbStop);
			dbStop.getPilgrims().add(dbPilgrim);
			int distance = randomDistance(0, 1000);
			dbPilgrim.getCarnet().setDistance(dbPilgrim.getCarnet().getDistance() + distance);
			stampStop = true;
		}

		// Crear estancia si es necesario
		if (createStay) {
			boolean stayExists = stayRepository.existsByPilgrim_IdAndStop_IdAndDate(dbPilgrim.getId(),
					dbStop.getId(), LocalDate.now());

			if (!stayExists) {
				Stay stay = new Stay(LocalDate.now(), isVip, dbPilgrim, dbStop);
				
				dbPilgrim.getStays().add(stay);
				System.out.println(dbPilgrim.getStays());

				// Incrementar VIP si corresponde
				if (isVip) {
					dbPilgrim.getCarnet().setnVips(dbPilgrim.getCarnet().getnVips() + 1);
				}
				stampStay = true;
			}
		}

		if (stampStop) {
			pilgrimRepository.save(dbPilgrim);
		}
		
		if (stampStop && stampStay) {
			return new ServiceResponse<>(true, dbPilgrim, "Parada y estancia sellada con éxito.");
		} else if (stampStop) {
			return new ServiceResponse<>(true, dbPilgrim, "Parada sellada con éxito.");
		} else {
			return new ServiceResponse<>(false, null, "No se ha podido actualizar el peregrino, ya ha sellado en la parada.");
		}

	}

	private int randomDistance(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

}
