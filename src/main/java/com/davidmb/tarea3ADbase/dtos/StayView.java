package com.davidmb.tarea3ADbase.dtos;

import java.time.LocalDate;

public class StayView {

	  
	    private String pilgrimName;
	    private String pilgrimNationality;
	    private Boolean stay; 
	    private LocalDate stayDate;
	    private Boolean isVip; 

	    public StayView( String pilgrimName, String pilgrimNationality, Boolean stay, LocalDate stayDate, Boolean isVip) {
	       
	        this.pilgrimName = pilgrimName;
	        this.pilgrimNationality = pilgrimNationality;
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
