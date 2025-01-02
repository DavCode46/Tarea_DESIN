package com.davidmb.tarea3ADbase.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.modelo.Pilgrim;


@Repository
public interface PilgrimRepository extends JpaRepository<Pilgrim, Long> {

	
}
