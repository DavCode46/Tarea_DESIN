/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidmb.tarea3ADbase.config;

import java.io.IOException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javafx.stage.Stage;

/**
 * Configuración principal de la aplicación basada en Spring.
 * 
 * Esta clase define los beans principales que serán gestionados por Spring, incluyendo:
 * - Cargador de vistas FXML con soporte para inyección de controladores de Spring.
 * - Bundle de recursos para internacionalización.
 * - Codificador de contraseñas usando BCrypt.
 * - Gestión de la ventana principal `StageManager` en JavaFX.
 * 
 * @author DavidMB
 */
@Configuration
public class AppJavaConfig {
	
    @Autowired 
    SpringFXMLLoader springFXMLLoader;



    /**
     * Proporciona un `ResourceBundle` para la internacionalización de la aplicación.
     * 
     * @return Un `ResourceBundle` cargado desde `Bundle.properties`.
     */
    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("Bundle");
    }
    
    /**
     * Proporciona un codificador de contraseñas seguro usando BCrypt.
     * 
     * Se utiliza en la autenticación de usuarios dentro de la aplicación.
     * 
     * @return Una instancia de `BCryptPasswordEncoder` para el cifrado de contraseñas.
     */
    @Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    /**
     * Gestiona la ventana principal (`Stage`) de la aplicación JavaFX.
     * 
     * Se inicializa de manera diferida (`@Lazy`) para asegurarse de que se crea después 
     * de que Spring haya cargado su contexto.
     * 
     * @param stage El `Stage` principal de JavaFX.
     * @return Una instancia de `StageManager` para gestionar las escenas en JavaFX.
     * @throws IOException Si ocurre un error al cargar la configuración inicial.
     */
    @Bean
    @Lazy(value = true) //Stage only created after Spring context bootstap
    public StageManager stageManager(Stage stage) throws IOException {
        return new StageManager(springFXMLLoader, stage);
    }

}
