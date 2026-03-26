package com.roombooker.fullstack_backend.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Rezervacija implements Serializable, DomainEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rezervacija", nullable = false, updatable = false, unique = true)
	private int rezervacijaId;

	@Column(name = "datum_prijave", nullable = false, updatable = true)
	private LocalDate datumPrijave;

	@Column(name = "datum_odjave", nullable = false, updatable = true)
	private LocalDate datumOdjave;

	@Column(name = "broj_osoba", nullable = false, updatable = true)
	private int brojOsoba;

	@Column(name = "ukupan_iznos", nullable = false, updatable = true)
	private double ukupanIznos;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true)
	private StatusRezervacije statusRezervacije;

	@Column(name = "datum_kreiranja", nullable = false, updatable = true)
	private LocalDate datumKreiranja;

	@ManyToOne
	@JoinColumn(name = "soba_id", nullable = false, updatable = true)
	private Soba soba;

	@ManyToOne
	@JoinColumn(name = "gost_id", nullable = false, updatable = true)
	private Gost gost;

	@ManyToOne
	@JoinColumn(name = "recepcionar_id", nullable = true, updatable = true)
	private Recepcionar recepcionar;

	public Rezervacija() {
	}

	public Rezervacija(int rezervacijaId, LocalDate datumPrijave, LocalDate datumOdjave, int brojOsoba,
			double ukupanIznos, StatusRezervacije statusRezervacije, LocalDate datumKreiranja, Soba soba, Gost gost,
			Recepcionar recepcionar) {
		this.rezervacijaId = rezervacijaId;
		this.datumPrijave = datumPrijave;
		this.datumOdjave = datumOdjave;
		this.brojOsoba = brojOsoba;
		this.ukupanIznos = ukupanIznos;
		this.statusRezervacije = statusRezervacije;
		this.datumKreiranja = datumKreiranja;
		this.soba = soba;
		this.gost = gost;
		this.recepcionar = recepcionar;
	}

	public int getRezervacijaId() {
		return rezervacijaId;
	}

	public void setRezervacijaId(int rezervacijaId) {
		this.rezervacijaId = rezervacijaId;
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

	public int getBrojOsoba() {
		return brojOsoba;
	}

	public void setBrojOsoba(int brojOsoba) {
		this.brojOsoba = brojOsoba;
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

	public Soba getSoba() {
		return soba;
	}

	public void setSoba(Soba soba) {
		this.soba = soba;
	}

	public Gost getGost() {
		return gost;
	}

	public void setGost(Gost gost) {
		this.gost = gost;
	}

	public Recepcionar getRecepcionar() {
		return recepcionar;
	}

	public void setRecepcionar(Recepcionar recepcionar) {
		this.recepcionar = recepcionar;
	}

	@Override
	public String toString() {
		return "Rezervacija{" + "rezervacijaId=" + rezervacijaId + ", datumPrijave=" + datumPrijave + ", datumOdjave="
				+ datumOdjave + ", brojOsoba=" + brojOsoba + ", ukupanIznos=" + ukupanIznos + ", statusRezervacije='"
				+ statusRezervacije + '\'' + ", datumKreiranja=" + datumKreiranja + ", soba="
				+ (soba != null ? soba.getBrojSobe() : null) + ", gost=" + (gost != null ? gost.getIme() : null)
				+ ", recepcionar=" + (recepcionar != null ? recepcionar.getIme() : null) + '}';
	}
}
