//package com.davidmb.tarea3ADbase.repositories;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.davidmb.tarea3ADbase.models.Pilgrim;
//import com.davidmb.tarea3ADbase.models.PilgrimStops;
//import com.davidmb.tarea3ADbase.models.Stop;
//
//
//@Repository
//public interface PilgrimStopsRepository extends JpaRepository<PilgrimStops, Long> {
//	boolean existsByPilgrimAndStopAndStopDate(Pilgrim pilgrim, Stop stop, LocalDate stopDate);
//	
//	 List<PilgrimStops> findAllByPilgrim_Id(Long id);
//}
