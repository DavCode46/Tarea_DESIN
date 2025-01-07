package com.davidmb.tarea3ADbase.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase que representa una estancia de un peregrino en una parada
 * (No todas las paradas tienen estancia)
 */
@Entity
@Table(name = "Estancias")
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "vip", nullable = false)
    private boolean vip = false;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_peregrino", referencedColumnName = "id")
    private Pilgrim pilgrim;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parada", referencedColumnName = "id")
    private Stop stop;

    public Stay() {
        super();
    }

    public Stay(LocalDate date, boolean vip, Pilgrim pilgrim, Stop stop) {
        super();
        this.date = date;
        this.vip = vip;
        this.pilgrim = pilgrim;
        this.stop = stop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Stay stay = (Stay) obj;
        return Objects.equals(id, stay.id) ||
               (Objects.equals(date, stay.date) && 
                stop != null && Objects.equals(stop, stay.stop));
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, date, stop); 
    }


    @Override
    public String toString() {
        return "Stay [id=" + id + ", date=" + date + ", vip=" + vip + ", pilgrim=" + pilgrim + ", stop=" + stop + "]";
    }
}
