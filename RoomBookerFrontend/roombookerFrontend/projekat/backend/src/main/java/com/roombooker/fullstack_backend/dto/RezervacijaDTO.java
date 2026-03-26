package com.roombooker.fullstack_backend.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.roombooker.fullstack_backend.model.StatusRezervacije;

public class RezervacijaDTO implements DomainDTO, Serializable {

	    private int idRezervacija;
	    private LocalDate datumPrijave;
	    private LocalDate datumOdjave;
	    private double ukupanIznos;
	    private StatusRezervacije statusRezervacije;
	    private LocalDate datumKreiranja;
	    private int brojOsoba;
	    private GostDTO gost;
	    private SobaDTO soba;
	    private RecepcionarDTO recepcionar;

	    public RezervacijaDTO() {
	    }

	    public RezervacijaDTO(int idRezervacija, LocalDate datumPrijave, LocalDate datumOdjave, 
	                          double ukupanIznos, StatusRezervacije statusRezervacije, LocalDate datumKreiranja, int brojOsoba, GostDTO gost, RecepcionarDTO recepcionar, SobaDTO soba) {
	        this.idRezervacija = idRezervacija;
	        this.datumPrijave = datumPrijave;
	        this.datumOdjave = datumOdjave;
	        this.ukupanIznos = ukupanIznos;
	        this.statusRezervacije = statusRezervacije;
	        this.datumKreiranja = datumKreiranja;
	        this.brojOsoba = brojOsoba;
	        this.gost=gost;
	        this.soba=soba;
	        this.recepcionar=recepcionar;
	    }
	    
	    

	    public GostDTO getGost() {
			return gost;
		}

		public void setGost(GostDTO gost) {
			this.gost = gost;
		}

		public SobaDTO getSoba() {
			return soba;
		}

		public void setSoba(SobaDTO soba) {
			this.soba = soba;
		}

		public RecepcionarDTO getRecepcionar() {
			return recepcionar;
		}

		public void setRecepcionar(RecepcionarDTO recepcionar) {
			this.recepcionar = recepcionar;
		}

		public int getIdRezervacija() {
	        return idRezervacija;
	    }

	    public void setIdRezervacija(int idRezervacija) {
	        this.idRezervacija = idRezervacija;
	    }

	    public LocalDate getDatumPrijave() {
	        return datumPrijave;
	    }

	    public void setDatumPrijave(LocalDate datumPrijave) {
	        this.datumPrijave = datumPrijave;
	    }

	    public LocalDate getDatumOdjave() {
	        return datumOdjave;
	    }

	    public void setDatumOdjave(LocalDate datumOdjave) {
	        this.datumOdjave = datumOdjave;
	    }

	    public double getUkupanIznos() {
	        return ukupanIznos;
	    }

	    public void setUkupanIznos(double ukupanIznos) {
	        this.ukupanIznos = ukupanIznos;
	    }

	    public StatusRezervacije getStatusRezervacije() {
	        return statusRezervacije;
	    }

	    public void setStatusRezervacije(StatusRezervacije statusRezervacije) {
	        this.statusRezervacije = statusRezervacije;
	    }

	    public LocalDate getDatumKreiranja() {
	        return datumKreiranja;
	    }

	    public void setDatumKreiranja(LocalDate datumKreiranja) {
	        this.datumKreiranja = datumKreiranja;
	    }

	    public int getBrojOsoba() {
	        return brojOsoba;
	    }

	    public void setBrojOsoba(int brojOsoba) {
	        this.brojOsoba = brojOsoba;
	    }

	    @Override
	    public String toString() {
	        return "RezervacijaDTO{" +
	                "idRezervacija=" + idRezervacija +
	                ", datumPrijave=" + datumPrijave +
	                ", datumOdjave=" + datumOdjave +
	                ", ukupanIznos=" + ukupanIznos +
	                ", statusRezervacije=" + statusRezervacije +
	                ", datumKreiranja=" + datumKreiranja +
	                ", brojOsoba=" + brojOsoba +
	                '}';
	    }
	}


