package com.davidmb.tarea3ADbase.models;
//
//import java.time.LocalDate;
//import java.util.Objects;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name = "peregrinos_paradas")
//public class PilgrimStops {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	@ManyToOne
//	@JoinColumn(name = "id_peregrino", nullable = false)
//	private Pilgrim pilgrim;
//	
//	@ManyToOne
//	@JoinColumn(name = "id_parada", nullable = false)
//	private Stop stop;
//	
//	@Column(name = "fecha_parada", nullable = false)
//	private LocalDate stopDate;
//	
//	public PilgrimStops() {
//		super();
//	}
//	
//	public PilgrimStops(Pilgrim pilgrim, Stop stop, LocalDate stopDate) {
//		super();
//		this.pilgrim = pilgrim;
//		this.stop = stop;
//		this.stopDate = stopDate;
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public Pilgrim getPilgrim() {
//		return pilgrim;
//	}
//
//	public void setPilgrim(Pilgrim pilgrim) {
//		this.pilgrim = pilgrim;
//	}
//
//	public Stop getStop() {
//		return stop;
//	}
//
//	public void setStop(Stop stop) {
//		this.stop = stop;
//	}
//
//	public LocalDate getStopDate() {
//		return stopDate;
//	}
//
//	public void setStopDate(LocalDate stopDate) {
//		this.stopDate = stopDate;
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(id, pilgrim, stop, stopDate);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		PilgrimStops other = (PilgrimStops) obj;
//		return Objects.equals(id, other.id) && Objects.equals(pilgrim, other.pilgrim)
//				&& Objects.equals(stop, other.stop) && Objects.equals(stopDate, other.stopDate);
//	}
//
//	@Override
//	public String toString() {
//		return "PilgrimStops [id=" + id + ", pilgrim=" + pilgrim + ", stop=" + stop + ", stopDate=" + stopDate + "]";
//	}
//	
//	
//	
//}

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "peregrinos_paradas")
@IdClass(PilgrimStopsId.class) 
public class PilgrimStops {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_peregrino", nullable = false)
    private Pilgrim pilgrim;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_parada", nullable = false)
    private Stop stop;

    @Id
    @Column(name = "fecha_parada", nullable = false)
    private LocalDate stopDate;

    public PilgrimStops() {}

    public PilgrimStops(Pilgrim pilgrim, Stop stop, LocalDate stopDate) {
        this.pilgrim = pilgrim;
        this.stop = stop;
        this.stopDate = stopDate;
    }
    
    

    public Pilgrim getPilgrim() {
		return pilgrim;
	}

	public void setPilgrim(Pilgrim pilgrim) {
		this.pilgrim = pilgrim;
	}

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public LocalDate getStopDate() {
		return stopDate;
	}

	public void setStopDate(LocalDate stopDate) {
		this.stopDate = stopDate;
	}


    @Override
    public int hashCode() {
        return Objects.hash(pilgrim, stop, stopDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PilgrimStops that = (PilgrimStops) obj;
        return Objects.equals(pilgrim, that.pilgrim) &&
               Objects.equals(stop, that.stop) &&
               Objects.equals(stopDate, that.stopDate);
    }
}
