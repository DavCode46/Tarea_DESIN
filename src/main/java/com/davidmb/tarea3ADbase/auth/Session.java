package com.davidmb.tarea3ADbase.auth;

import org.springframework.stereotype.Component;

import com.davidmb.tarea3ADbase.models.User;

@Component
public class Session {
	private User loggedInUser;
	
	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	public void setLoggedInUser(User user) {
		this.loggedInUser = user;
	}
	
}
