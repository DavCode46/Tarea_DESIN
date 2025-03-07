package com.davidmb.tarea3ADbase.batch;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.repositories.UserRepository;

/**
 * Clase encargada de insertar usuarios por defecto en la base de datos al iniciar la aplicación.
 * 
 * Esta clase implementa {@link CommandLineRunner}, lo que permite ejecutar código automáticamente
 * al iniciar la aplicación Spring Boot. Se verifica si la base de datos ya contiene usuarios antes
 * de insertar los usuarios predefinidos.
 * 
 * Los usuarios insertados incluyen un administrador y varias paradas.
 * 
 * @author DavidMB
 */
@Component
public class BatchUserInserter implements CommandLineRunner {

	/** Repositorio para la gestión de usuarios en la base de datos */
	private final UserRepository userRepository;
	  
    /** Codificador de contraseñas para almacenar las contraseñas de forma segura */
	private final PasswordEncoder passwordEncoder;

	 /**
     * Constructor de la clase que inyecta el repositorio de usuarios y el codificador de contraseñas.
     * 
     * @param userRepository Repositorio de usuarios.
     * @param passwordEncoder Codificador de contraseñas.
     */
	public BatchUserInserter(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	  /**
     * Método que se ejecuta automáticamente al iniciar la aplicación.
     * 
     * Si la base de datos no contiene usuarios, inserta un conjunto predefinido de usuarios con roles específicos.
     * 
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     * @throws Exception Si ocurre un error durante la inserción de usuarios.
     */
	@Override
	public void run(String... args) throws Exception {
		if (userRepository.count() == 0) {
			List<User> users = Arrays.asList(
					 new User("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("Usuario4646@")),					
					 new User("Siero", "Parada", "siero@parada.com", passwordEncoder.encode("Usuario4646@")),
					 new User("Oviedo", "Parada", "oviedo@parada.com", passwordEncoder.encode("Usuario4646@")),
					 new User("Gijón", "Parada", "gijon@parada.com", passwordEncoder.encode("Usuario4646@")),
					 new User("Nava", "Parada", "nava@parada.com", passwordEncoder.encode("Usuario4646@")));

			userRepository.saveAll(users);
		}
	}
}
