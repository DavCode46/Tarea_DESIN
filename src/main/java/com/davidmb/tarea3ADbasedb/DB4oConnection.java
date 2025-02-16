package com.davidmb.tarea3ADbasedb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

@Component
public class DB4oConnection {
    private static DB4oConnection INSTANCE = null;
    private static ObjectContainer db;
    private static String DB_PATH;

    // Constructor privado
    private DB4oConnection() {}

    // Obtener instancia de conexión, creando si es necesario
    public static synchronized DB4oConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DB4oConnection();
            INSTANCE.loadConfig();
            INSTANCE.performConnection();
        }
        return INSTANCE;
    }

    // Cargar configuración desde db4o.properties
    private void loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db4o.properties")) {
            if (input != null) {
                properties.load(input);
                DB_PATH = properties.getProperty("db4o.path");
                if (DB_PATH == null || DB_PATH.isEmpty()) {
                    throw new IOException("La ruta de la base de datos no está configurada en db4o.properties");
                }
            } else {
                throw new IOException("No se pudo encontrar el archivo db4o.properties");
            }
        } catch (IOException e) {
            System.err.println("Error cargando db4o.properties: " + e.getMessage());
            throw new RuntimeException("Fallo al cargar configuración de base de datos", e);
        }
    }

    // Establecer la conexión a la base de datos
    private void performConnection() {
        try {
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_PATH);
            System.out.println("Conexión establecida con DB4O en: " + DB_PATH);
        } catch (Exception e) {
            System.err.println("Error al abrir la conexión con la base de datos: " + e.getMessage());
            throw new RuntimeException("Fallo al conectar con la base de datos", e);
        }
    }

    // Obtener la instancia de la base de datos
    public ObjectContainer getDb() {
        return db;
    }

    // Cerrar la conexión
    public void closeConnection() {
        if (db != null) {
            db.close();
            System.out.println("Conexión DB4O cerrada.");
        }
    }
}
