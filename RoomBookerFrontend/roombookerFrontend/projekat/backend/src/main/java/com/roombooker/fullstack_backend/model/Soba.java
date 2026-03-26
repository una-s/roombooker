package com.roombooker.fullstack_backend.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Soba implements Serializable, DomainEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_soba", nullable = false, updatable = false, unique = true)
	private int idSoba;

	@Column(name="broj_sobe", nullable = false, unique = true)
	private int brojSobe;

	@Column(name="sprat", nullable = false)
	private int sprat;

	@Column(name="dostupna", nullable = false)
	private boolean dostupna;
	
    @Column(name = "slika", nullable = false)
    private String slika;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tip_sobe", nullable = false)
	private TipSobe tipSobe;

	public int getIdSoba() {
		return idSoba;
	}

	public void setIdSoba(int idSoba) {
		this.idSoba = idSoba;
	}

	public int getBrojSobe() {
		return brojSobe;
	}
	
	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
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

	public boolean isDostupna() {
		return dostupna;
	}

	public void setDostupna(boolean dostupna) {
		this.dostupna = dostupna;
	}

	public TipSobe getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}

}
