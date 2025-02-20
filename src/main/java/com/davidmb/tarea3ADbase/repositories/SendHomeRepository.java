package com.davidmb.tarea3ADbase.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.db.ObjectDBConnection;
import com.davidmb.tarea3ADbase.models.SendHome;

import jakarta.persistence.EntityManager;

@Repository
public class SendHomeRepository {

	public void save(SendHome sendHome) {
	    if (sendHome.getAddress() == null) {
	        System.out.println("Error: El campo Address es null antes de guardar.");
	    } else {
	        System.out.println("Address: " + sendHome.getAddress().toString());
	    }

	    EntityManager em = ObjectDBConnection.getEntityManager();
	    em.getTransaction().begin();
	    em.persist(sendHome);	
	    em.getTransaction().commit();
	    em.close();
	}

	
	public List<SendHome> getAllByStopId(Long stopId) {
	    EntityManager em = ObjectDBConnection.getEntityManager();
	    List<SendHome> sendHomes = em.createQuery(
	            "SELECT s FROM SendHome s WHERE :stopId = s.stopId", SendHome.class)
	            .setParameter("stopId", stopId)
	            .getResultList();
	    em.close();
	    return sendHomes;
	}

	
	public SendHome findById(Long id) {
	    EntityManager em = ObjectDBConnection.getEntityManager();
	    SendHome sendHome = em.find(SendHome.class, id); 
	    em.close();
	    return sendHome;
	}


}
