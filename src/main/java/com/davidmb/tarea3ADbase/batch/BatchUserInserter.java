package com.davidmb.tarea3ADbase.batch;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.repositories.UserRepository;

@Component
public class BatchUserInserter implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public BatchUserInserter(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

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
