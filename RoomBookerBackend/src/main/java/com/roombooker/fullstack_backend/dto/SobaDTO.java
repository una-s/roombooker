package com.roombooker.fullstack_backend.dto;

import java.io.Serializable;

public class SobaDTO implements Serializable {

	private int idSoba;
	private boolean dostupna;
	private int brojSobe;
	private int sprat;
	private TipSobeDTO tipSobe;
	private String slika;
	

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public int getIdSoba() {
		return idSoba;
	}

	public void setIdSoba(int idSoba) {
		this.idSoba = idSoba;
	}

	public boolean isDostupna() {
		return dostupna;
	}

	public void setDostupna(boolean dostupna) {
		this.dostupna = dostupna;
	}

	public int getBrojSobe() {
		return brojSobe;
	}

	public void setBrojSobe(int brojSobe) {
		this.brojSobe = brojSobe;
	}

	public int getSprat() {
		return sprat;
	}

	public void setSprat(int sprat) {
		this.sprat = sprat;
	}

	public TipSobeDTO getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobeDTO tipSobe) {
		this.tipSobe = tipSobe;
	}
}
