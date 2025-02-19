package com.davidmb.tarea3ADbase.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SendHome extends Service{
	@Id
	Long id;
	double weight;
	int[] volume = new int[3];
	boolean urgent = false;
	@Embedded
	Address address;
	
	public SendHome() {
	}
	
	public SendHome( double price, int weight, int[] volume, boolean urgent, Address address) {
		super("Envío a casa", price, new ArrayList<>());
		this.weight = weight;
		this.volume = volume;
		this.urgent = urgent;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int[] getVolume() {
		return volume;
	}

	public void setVolume(int[] volume) {
		this.volume = volume;
	}

	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(volume);
		result = prime * result + Objects.hash(address, id, urgent, weight);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SendHome other = (SendHome) obj;
		return Objects.equals(address, other.address) && Objects.equals(id, other.id) && urgent == other.urgent
				&& Arrays.equals(volume, other.volume)
				&& Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight);
	}

	@Override
	public String toString() {
		return "SendHome [id=" + id + ", weight=" + weight + ", volume=" + Arrays.toString(volume) + ", urgent="
				+ urgent + ", address=" + address + "]";
	}	
}
