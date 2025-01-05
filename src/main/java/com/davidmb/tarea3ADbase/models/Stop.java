package com.davidmb.tarea3ADbase.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa una parada, en cada parada se almacena una lista de peregrinos
 */
@Entity
@Table(name = "Paradas")
public class Stop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "region", nullable = false, length = 2)
    private char region;

    @Column(name = "responsable", nullable = false, length = 100)
    private String manager;

    @Column(name = "id_usuario", nullable = true)
    private Long userId;

    @ManyToMany(mappedBy = "stops", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pilgrim> pilgrims = new ArrayList<>();


    public Stop() {
        super();
    }

    public Stop(String name, char region, String manager) {
        super();
        this.name = name;
        this.region = region;
        this.manager = manager;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getRegion() {
        return region;
    }

    public void setRegion(char region) {
        this.region = region;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Pilgrim> getPilgrims() {
        return pilgrims;
    }

    public void setPilgrims(List<Pilgrim> pilgrims) {
        this.pilgrims = pilgrims;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manager, name, pilgrims, region, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Stop other = (Stop) obj;
        return Objects.equals(id, other.id) && Objects.equals(manager, other.manager)
                && Objects.equals(name, other.name) && Objects.equals(pilgrims, other.pilgrims)
                && region == other.region && Objects.equals(userId, other.userId);
    }

    @Override
    public String toString() {
        return "Stop [id=" + id + ", name=" + name + ", region=" + region + ", manager=" + manager + ", userId="
                + userId + ", pilgrims=" + pilgrims + "]";
    }
}
