package com.davidmb.tarea3ADbase.dtos;

import java.time.LocalDate;

public class StayView {

	private String pilgrimName;
	private String pilgrimNationality;
	private LocalDate stopDate;
	private Boolean stay;
	private LocalDate stayDate;
	private Boolean isVip;

	public StayView(String pilgrimName, String pilgrimNationality, LocalDate stopDate, Boolean stay, LocalDate stayDate,
			Boolean isVip) {

		this.pilgrimName = pilgrimName;
		this.pilgrimNationality = pilgrimNationality;
		this.stopDate = stopDate;
		this.stay = stay;
		this.stayDate = stayDate;
		this.isVip = isVip;
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

	public LocalDate getStopDate() {
		return stopDate;
	}

	public void setStopDate(LocalDate stopDate) {
		this.stopDate = stopDate;
	}

	public Boolean getStay() {
		return stay;
	}

	public void setStay(Boolean stay) {
		this.stay = stay;
	}

	public LocalDate getStayDate() {
		return stayDate;
	}

	public void setStayDate(LocalDate stayDate) {
		this.stayDate = stayDate;
	}

	public Boolean getIsVip() {
		return isVip;
	}

	public void setIsVip(Boolean isVip) {
		this.isVip = isVip;
	}

}
