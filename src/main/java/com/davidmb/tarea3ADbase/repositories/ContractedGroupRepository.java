package com.davidmb.tarea3ADbase.repositories;

import com.davidmb.tarea3ADbase.models.ContractedGroup;
import com.davidmb.tarea3ADbasedb.DB4oConnection;
import com.db4o.ObjectContainer;

public class ContractedGroupRepository {
	public void save(ContractedGroup contractedGroup) {
		ObjectContainer db = DB4oConnection.getInstance().getDb();
	}
}
