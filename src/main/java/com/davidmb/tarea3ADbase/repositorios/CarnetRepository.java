package com.davidmb.tarea3ADbase.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.modelo.Carnet;


@Repository
public interface CarnetRepository extends JpaRepository<Carnet, Long> {

	
}
