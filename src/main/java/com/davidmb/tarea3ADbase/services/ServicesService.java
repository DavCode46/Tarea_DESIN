package com.davidmb.tarea3ADbase.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.davidmb.tarea3ADbase.models.Service;
import com.davidmb.tarea3ADbase.repositories.ServicesRepository;

@org.springframework.stereotype.Service
public class ServicesService {
    private ServicesRepository servicesRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

	public void save(Service service) {
		servicesRepository.save(service);
	}
	
	public void update(Service service) {
		servicesRepository.update(service);
	}

	public List<Service> findAll() {
		return servicesRepository.findAll();
	}
	
	public Service findById(Long id) {
		return servicesRepository.findById(id);
	}
	
	
	public Service findByName(String name) {
		return servicesRepository.findByName(name);
	}
	
	public List<Service> findByStopId(Long id) {
		return servicesRepository.findByStopId(id);
	}
	
	public boolean checkDisponibility(String name, Long stopId) {
		return servicesRepository.checkDisponibility(name, stopId);
	}

	public Long getMaxId() {
		return servicesRepository.getMaxId();
	}
}
