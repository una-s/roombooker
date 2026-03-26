package com.roombooker.fullstack_backend.dto;

import java.io.Serializable;

public class TipSobeDTO implements Serializable {

	private int idTipSobe;
	private String naziv;
	private String opis;
	private double cenaPoNoci;


	public int getIdTipSobe() {
		return idTipSobe;
	}

	public void setIdTipSobe(int idTipSobe) {
		this.idTipSobe = idTipSobe;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public double getCenaPoNoci() {
		return cenaPoNoci;
	}

	public void setCenaPoNoci(double cenaPoNoci) {
		this.cenaPoNoci = cenaPoNoci;
	}

}
