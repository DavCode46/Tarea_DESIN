package com.davidmb.tarea3ADbase.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.modelo.User;
import com.davidmb.tarea3ADbase.repositorios.UserRepository;

@Service
public class UserService {

	private final PasswordEncoder passwordEncoder;

	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	private UserRepository userRepository;

	public User save(User entity) {
	    if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
	        entity.setPassword(encodePassword(entity.getPassword()));
	    }
	    return userRepository.save(entity);
	}

	public User update(User entity) {
	    if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
	        entity.setPassword(encodePassword(entity.getPassword()));
	    }
	    return userRepository.save(entity);
	}


	public void delete(User entity) {
		userRepository.delete(entity);
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	public void resetPassword(User user, String newPassword) {
	    if (!isPasswordValid(newPassword)) {
	        throw new IllegalArgumentException("La nueva contraseña no cumple con los requisitos de seguridad.");
	    }
	    String encodedPassword = passwordEncoder.encode(newPassword);
	    user.setPassword(encodedPassword);
	    userRepository.save(user);
	}


	private boolean isPasswordValid(String password) {
		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
		return password != null && password.matches(passwordPattern);
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password); 
	}

	public User find(Long id) {
		return userRepository.findById(id).get();
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public boolean authenticate(String username, String rawPassword) {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			return false;
		}
		return passwordEncoder.matches(rawPassword, user.getPassword());
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void deleteInBatch(List<User> users) {
		userRepository.deleteAll(users);
	}

}
