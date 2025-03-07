package com.davidmb.tarea3ADbase.dtos;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) que representa la vista de una estancia (`Stay`).
 * 
 * Este DTO encapsula información sobre las estancias de los peregrinos en una parada,
 * incluyendo detalles como el nombre del peregrino, su nacionalidad, la fecha de la parada,
 * si se alojó en la parada, la fecha de la estancia y si la estancia fue VIP.
 * 
 * Se usa para transferir datos entre la capa de persistencia y la capa de presentación
 * sin exponer directamente la entidad `Stay`.
 * 
 * @author DavidMB
 */
public class StayView {

    /** Nombre del peregrino. */
    private String pilgrimName;

    /** Nacionalidad del peregrino. */
    private String pilgrimNationality;

    /** Fecha en la que el peregrino visitó la parada. */
    private LocalDate stopDate;

    /** Indica si el peregrino se alojó en la parada (`true` si se alojó, `false` si no). */
    private Boolean stay;

    /** Fecha en la que el peregrino se alojó en la parada (si aplicable). */
    private LocalDate stayDate;

    /** Indica si la estancia del peregrino fue VIP. */
    private Boolean isVip;

    /**
     * Constructor de la clase `StayView`.
     * 
     * @param pilgrimName Nombre del peregrino.
     * @param pilgrimNationality Nacionalidad del peregrino.
     * @param stopDate Fecha en la que el peregrino visitó la parada.
     * @param stay Indica si el peregrino se alojó en la parada.
     * @param stayDate Fecha en la que el peregrino se alojó en la parada.
     * @param isVip Indica si la estancia fue VIP.
     */
    public StayView(String pilgrimName, String pilgrimNationality, LocalDate stopDate, 
                    Boolean stay, LocalDate stayDate, Boolean isVip) {
        this.pilgrimName = pilgrimName;
        this.pilgrimNationality = pilgrimNationality;
        this.stopDate = stopDate;
        this.stay = stay;
        this.stayDate = stayDate;
        this.isVip = isVip;
    }

    /**
     * Obtiene el nombre del peregrino.
     * 
     * @return Nombre del peregrino.
     */
    public String getPilgrimName() {
        return pilgrimName;
    }

    /**
     * Establece el nombre del peregrino.
     * 
     * @param pilgrimName Nombre del peregrino.
     */
    public void setPilgrimName(String pilgrimName) {
        this.pilgrimName = pilgrimName;
    }

    /**
     * Obtiene la nacionalidad del peregrino.
     * 
     * @return Nacionalidad del peregrino.
     */
    public String getPilgrimNationality() {
        return pilgrimNationality;
    }

    /**
     * Establece la nacionalidad del peregrino.
     * 
     * @param pilgrimNationality Nacionalidad del peregrino.
     */
    public void setPilgrimNationality(String pilgrimNationality) {
        this.pilgrimNationality = pilgrimNationality;
    }

    /**
     * Obtiene la fecha en la que el peregrino visitó la parada.
     * 
     * @return Fecha de visita a la parada.
     */
    public LocalDate getStopDate() {
        return stopDate;
    }

    /**
     * Establece la fecha en la que el peregrino visitó la parada.
     * 
     * @param stopDate Fecha de visita a la parada.
     */
    public void setStopDate(LocalDate stopDate) {
        this.stopDate = stopDate;
    }

    /**
     * Obtiene si el peregrino se alojó en la parada.
     * 
     * @return `true` si se alojó, `false` en caso contrario.
     */
    public Boolean getStay() {
        return stay;
    }

    /**
     * Establece si el peregrino se alojó en la parada.
     * 
     * @param stay `true` si se alojó, `false` en caso contrario.
     */
    public void setStay(Boolean stay) {
        this.stay = stay;
    }

    /**
     * Obtiene la fecha en la que el peregrino se alojó en la parada.
     * 
     * @return Fecha de la estancia.
     */
    public LocalDate getStayDate() {
        return stayDate;
    }

    /**
     * Establece la fecha en la que el peregrino se alojó en la parada.
     * 
     * @param stayDate Fecha de la estancia.
     */
    public void setStayDate(LocalDate stayDate) {
        this.stayDate = stayDate;
    }

    /**
     * Obtiene si la estancia fue VIP.
     * 
     * @return `true` si la estancia fue VIP, `false` en caso contrario.
     */
    public Boolean getIsVip() {
        return isVip;
    }

    /**
     * Establece si la estancia fue VIP.
     * 
     * @param isVip `true` si la estancia fue VIP, `false` en caso contrario.
     */
    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }
}
