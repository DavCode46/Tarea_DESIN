package com.davidmb.tarea3ADbase.controller;

import java.io.File;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Main {
	public static void main(String[] args) {
		
		File f = new File("personas.db4o");
		Persona p = new Persona("David", "Martín", 21);
		Persona p2 = new Persona("Juan", "García", 34);
		Persona p3 = new Persona("Pedro", "Pérez", 45);
		
		ObjectContainer db = Db4o.openFile(f.getAbsolutePath());
		
		db.store(p);
		db.store(p2);
		db.store(p3);
	
		
		Persona p4 = new Persona();
		Persona p5 = new Persona(null, null, 34);
		ObjectSet<Persona> result = db.queryByExample(p5);
		
		while(result.hasNext()) {
			System.out.println(result.next());
		}
		db.close();
	}
	
	
}

class Persona {
	String nombre;
	String apellidos;
	int edad;
	
	public Persona() {
	}
	
	public Persona(String nombre, String apellidos, int edad) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.edad = edad;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public int getEdad() {
		return edad;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public String toString() {
		return nombre + " " + apellidos + " " + edad;
	}
}

