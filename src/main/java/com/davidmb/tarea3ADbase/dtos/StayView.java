package com.davidmb.tarea3ADbase.dtos;

import java.time.LocalDate;

public class StayView {

	private Long stopId;
	private String pilgrimName;
	private String pilgrimNationality;
	private LocalDate staydate;
	private boolean isVip;
	
	public StayView(Long stopId, String pilgrimName, String pilgrimNationality, LocalDate staydate, boolean isVip) {
		super();
		this.stopId = stopId;
		this.pilgrimName = pilgrimName;
		this.pilgrimNationality = pilgrimNationality;
		this.staydate = staydate;
		this.isVip = isVip;
	}

	public Long getStopId() {
		return stopId;
	}

	public void setStopId(Long stopId) {
		this.stopId = stopId;
	}

	public String getPilgrimName() {
		return pilgrimName;
	}

	public void setPilgrimName(String pilgrimName) {
		this.pilgrimName = pilgrimName;
	}

	public String getPilgrimNationality() {
		return pilgrimNationality;
	}

	public void setPilgrimNationality(String pilgrimNationality) {
		this.pilgrimNationality = pilgrimNationality;
	}

	public LocalDate getStaydate() {
		return staydate;
	}

	public void setStaydate(LocalDate staydate) {
		this.staydate = staydate;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}
	
	
}
