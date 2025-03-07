package com.davidmb.tarea3ADbase;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX con integración en Spring Boot.
 * 
 * Esta clase inicia el contexto de Spring Boot y gestiona la carga de la 
 * interfaz de usuario de JavaFX. Funciona como el punto de entrada de la 
 * aplicación y se encarga de inicializar y mostrar la escena inicial.
 * 
 * La arquitectura permite que Spring gestione las dependencias, mientras 
 * que JavaFX maneja la interfaz gráfica.
 * 
 * @author DavidMB
 */
@SpringBootApplication
public class Tarea3AdbaseApplication extends Application {

	 /** Contexto de la aplicación Spring Boot */
	protected ConfigurableApplicationContext springContext;
	 /** Gestor de escenas para manejar la navegación en JavaFX */
	protected StageManager stageManager;

	 /**
     * Inicializa el contexto de Spring antes de que inicie la interfaz gráfica de JavaFX.
     * 
     * @throws Exception Si ocurre un error al iniciar el contexto de Spring.
     */
	@Override
	public void init() throws Exception {
		springContext = springBootApplicationContext();
	}

	 /**
     * Método principal que lanza la aplicación JavaFX.
     * 
     * @param args Argumentos de línea de comandos.
     */
	public static void main(final String[] args) {
		Application.launch(args);
	}

	 /**
     * Inicia la ventana principal de JavaFX y carga la escena inicial.
     * 
     * @param primaryStage La ventana principal de la aplicación.
     * @throws Exception Si ocurre un error durante la carga de la interfaz gráfica.
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stageManager = springContext.getBean(StageManager.class, primaryStage);
		displayInitialScene();

	}

	/**
     * Muestra la escena inicial de la aplicación.
     * 
     * Este método puede ser sobrescrito por subclases para cambiar la primera escena
     * que se muestra al iniciar la aplicación. Por defecto, carga la vista de login.
     */
	protected void displayInitialScene() {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	 /**
     * Crea e inicia el contexto de la aplicación Spring Boot.
     * 
     * @return El contexto de la aplicación `ConfigurableApplicationContext`.
     */
	private ConfigurableApplicationContext springBootApplicationContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Tarea3AdbaseApplication.class);
		String[] args = getParameters().getRaw().stream().toArray(String[]::new);
		return builder.run(args);
	}

}
