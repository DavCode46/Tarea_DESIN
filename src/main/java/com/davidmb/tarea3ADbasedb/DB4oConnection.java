package com.davidmb.tarea3ADbasedb;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DB4oConnection {
    private static DB4oConnection INSTANCE = null;
    private static ObjectContainer db;
    private static String DB_PATH;


    private DB4oConnection() {}

    
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DB4oConnection();
            INSTANCE.loadConfig();
            INSTANCE.performConnection();
        }
    }

    
    public static ObjectContainer getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return db;
    }

    
    private void loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db4o.properties")) {
            if (input != null) {
                properties.load(input);
                DB_PATH = properties.getProperty("db4o.path");
            } 
        } catch (IOException e) {
            System.err.println("Error cargando db4o.properties: " + e.getMessage());
        }
    }


    public void performConnection() {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_PATH);
        System.out.println("Conexión establecida con DB4O en: " + DB_PATH);
    }

 
    public void closeConnection() {
        if (db != null) {
            db.close();
            System.out.println("Conexión DB4O cerrada.");
        }
    }
}
