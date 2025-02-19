package com.davidmb.tarea3ADbase.models;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Address {
	@Id
	Long id;
	String street;
	String locality;
	
	public Address() {
	}
	
	public Address(String street, String locality) {
		this.street = street;
		this.locality = locality;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, locality, street);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(id, other.id) && Objects.equals(locality, other.locality)
				&& Objects.equals(street, other.street);
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", locality=" + locality + "]";
	}
}
