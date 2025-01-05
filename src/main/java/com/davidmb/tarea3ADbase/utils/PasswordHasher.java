package com.davidmb.tarea3ADbase.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
	public static void main(String args[]) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		String rawPassword = "Admin1234@";
		
		String hashedPassword = passwordEncoder.encode(rawPassword);
		
		System.out.println(hashedPassword);
	}
}
