package com.davidmb.tarea3ADbase.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.models.ContractedGroup;
import com.davidmb.tarea3ADbase.repositories.ContractedGroupRepository;

@Service
public class ContractedGroupService {
	@Autowired
	private ContractedGroupRepository contractedGroupRepository;
	
	public boolean save(ContractedGroup contractedGroup) {
		return contractedGroupRepository.save(contractedGroup);
	}
	
	public Long getNextId() {
		return contractedGroupRepository.getNextId();
	}
}
