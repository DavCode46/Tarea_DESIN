package com.davidmb.tarea3ADbase.config;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Cargador de archivos FXML integrado con Spring.
 * 
 * Esta clase permite la carga de vistas FXML en una aplicación JavaFX que usa Spring,
 * integrando los controladores de JavaFX con el contexto de Spring. 
 * 
 * - Usa `ApplicationContext` para gestionar la inyección de dependencias en los controladores de FXML.
 * - Carga los archivos FXML y los asocia con sus respectivos controladores de Spring.
 * - Soporta la carga de `ResourceBundle` para internacionalización.
 * 
 * @author DavidMB
 */

@Component
public class SpringFXMLLoader {
    private final ResourceBundle resourceBundle;
    private final ApplicationContext context;

    /**
     * Constructor que inyecta el contexto de Spring y los recursos de internacionalización.
     *
     * @param context         Contexto de la aplicación Spring, que gestiona la inyección de dependencias.
     * @param resourceBundle  Paquete de recursos utilizado para la internacionalización de la aplicación.
     */
    @Autowired
    public SpringFXMLLoader(ApplicationContext context, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.context = context;
    }

    /**
     * Carga un archivo FXML y lo asocia con su controlador gestionado por Spring.
     *
     * @param fxmlPath Ruta del archivo FXML dentro del classpath.
     * @return La raíz del árbol de nodos JavaFX cargado desde el archivo FXML.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    public Parent load(String fxmlPath) throws IOException {      
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean); //Spring now FXML Controller Factory
        loader.setResources(resourceBundle);
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }
}
