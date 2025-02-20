package com.davidmb.tarea3ADbase.repositories;

import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.db.DB4oConnection;
import com.davidmb.tarea3ADbase.models.ContractedGroup;
import com.db4o.ObjectContainer;

@Repository
public class ContractedGroupRepository {
	public boolean save(ContractedGroup contractedGroup) {
		ObjectContainer db = DB4oConnection.getInstance().getDb();
		try {
			db.store(contractedGroup);
			db.commit();
			return true;
		} catch (Exception e) {
			db.rollback();
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
	}

	public Long getNextId() {
		ObjectContainer db = DB4oConnection.getInstance().getDb();
		try {
			long count = db.query(ContractedGroup.class).size();
			return count + 1;
		} finally {
			db.close();
		}
	}
}
