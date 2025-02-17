package com.davidmb.tarea3ADbase.utils;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Db4oReader {
	  public static void main(String[] args) {
	        ObjectContainer db = Db4o.openFile("db.db4o");
	        
	        try {
	            System.out.println("Contenido de la base de datos:");
	            ObjectSet<Object> result = db.queryByExample(new Object());
	            while (result.hasNext()) {
	                System.out.println(result.next());
	            }
	        } finally {
	            db.close();
	        }
	    }
}
