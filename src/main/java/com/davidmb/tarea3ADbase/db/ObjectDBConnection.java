package com.davidmb.tarea3ADbase.db;

import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Component
public class ObjectDBConnection {
    private static final EntityManagerFactory emf;

    static {
        Properties properties = new Properties();
        try {
            properties.load(ObjectDBConnection.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo application.properties", e);
        }
        String dbPath = properties.getProperty("odb.path");
        emf = Persistence.createEntityManagerFactory(dbPath);
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
