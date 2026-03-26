package com.roombooker.fullstack_backend.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tip_sobe")
public class TipSobe implements Serializable, DomainEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tip_sobe", nullable = false, updatable = false, unique = true)
	private int idTipSobe;

	@Column(name="naziv", nullable = false)
	private String naziv;

	@Column(name="cena_po_noci",nullable = false)
	private double cenaPoNoci;

	@Column(name="opis",length = 512)
	private String opis;

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

	public double getCenaPoNoci() {
		return cenaPoNoci;
	}

	public void setCenaPoNoci(double cenaPoNoci) {
		this.cenaPoNoci = cenaPoNoci;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
}
