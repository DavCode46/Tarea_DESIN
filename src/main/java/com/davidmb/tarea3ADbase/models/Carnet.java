package com.davidmb.tarea3ADbase.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase que representa un carnet de peregrino
 * (El carnet se crea en la primera parada que hace un peregrino)
 */
@Entity
@Table(name = "Carnets")
public class Carnet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fechaExpedicion", nullable = false)
    private LocalDate doExp = LocalDate.now();

    @Column(name = "distancia", nullable = false)
    private double distance = 0.0;

    @Column(name = "nVips", nullable = false)
    private int nVips = 0;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "paradaInicial", referencedColumnName = "id")
    private Stop initialStop;

    public Carnet() {
        super();
    }

    public Carnet(Stop initialStop) {
        super();
        this.initialStop = initialStop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDoExp() {
        return doExp;
    }

    public void setDoExp(LocalDate doExp) {
        this.doExp = doExp;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getnVips() {
        return nVips;
    }

    public void setnVips(int nVips) {
        this.nVips = nVips;
    }

    public Stop getInitialStop() {
        return initialStop;
    }

    public void setInitialStop(Stop initialStop) {
        this.initialStop = initialStop;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, doExp, id, initialStop, nVips);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Carnet other = (Carnet) obj;
        return Double.doubleToLongBits(distance) == Double.doubleToLongBits(other.distance)
                && Objects.equals(doExp, other.doExp) && Objects.equals(id, other.id)
                && Objects.equals(initialStop, other.initialStop) && nVips == other.nVips;
    }

    @Override
    public String toString() {
        return "Carnet [id=" + id + ", doExp=" + doExp + ", distance=" + distance + ", nVips=" + nVips
                + ", initialStop=" + (initialStop != null ? initialStop.getName() : "null") + "]";
    }
}
