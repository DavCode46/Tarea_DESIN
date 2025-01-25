package com.davidmb.tarea3ADbase.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PilgrimStopsId implements Serializable {

    private Long pilgrim;
    private Long stop;
    private LocalDate stopDate;

    public PilgrimStopsId() {}

    public PilgrimStopsId(Long pilgrim, Long stop, LocalDate stopDate) {
        this.pilgrim = pilgrim;
        this.stop = stop;
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
        PilgrimStopsId that = (PilgrimStopsId) obj;
        return Objects.equals(pilgrim, that.pilgrim) &&
               Objects.equals(stop, that.stop) &&
               Objects.equals(stopDate, that.stopDate);
    }
}
