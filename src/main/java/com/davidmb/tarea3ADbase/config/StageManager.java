package com.davidmb.tarea3ADbase.config;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.slf4j.Logger;

import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Clase que gestiona la navegación entre escenas en la aplicación JavaFX.
 * 
 * Se encarga de cambiar las vistas (`Scene`) dentro del `Stage` principal de la aplicación, 
 * asegurando que las vistas FXML sean cargadas correctamente mediante `SpringFXMLLoader`.
 * 
 * - Permite cambiar entre vistas de manera dinámica.
 * - Configura la ventana principal con tamaño y posición adecuados.
 * - Maneja errores en la carga de vistas y cierra la aplicación en caso de fallo crítico.
 * 
 * @author DavidMB
 */
public class StageManager {

    private static final Logger LOG = getLogger(StageManager.class);
    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    /**
     * Constructor de `StageManager`.
     * 
     * @param springFXMLLoader Cargador de vistas FXML integrado con Spring.
     * @param stage `Stage` principal donde se mostrarán las vistas.
     */
    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    /**
     * Cambia la escena actual a la vista especificada.
     * 
     * @param view Enumeración `FxmlView` que contiene la información de la vista a cargar.
     */
    public void switchScene(final FxmlView view) {
    	System.out.println("Cargando vista desde: " + view.getFxmlFile());
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }
    
    /**
     * Muestra la nueva escena en la ventana principal.
     * 
     * @param rootnode Nodo raíz de la vista a mostrar.
     * @param title Título de la ventana.
     */
    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
      //  primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
      //  primaryStage.setMaximized(true);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/pilgrim.png")));
        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }
    
    /**
     * Prepara y devuelve la escena correspondiente al nodo raíz proporcionado.
     * 
     * @param rootnode Nodo raíz de la nueva escena.
     * @return `Scene` configurada con el nodo raíz.
     */
    private Scene prepareScene(Parent rootnode){
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    /**
     * Carga la jerarquía de nodos desde un archivo FXML y devuelve su nodo raíz.
     * 
     * @param fxmlFilePath Ruta del archivo FXML a cargar.
     * @return Nodo raíz (`Parent`) de la vista cargada.
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }
    
    /**
     * Registra un error en los logs y cierra la aplicación en caso de fallo crítico.
     * 
     * @param errorMsg Mensaje de error.
     * @param exception Excepción ocurrida.
     */
    private void logAndExit(String errorMsg, Exception exception) {
        LOG.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }

}
