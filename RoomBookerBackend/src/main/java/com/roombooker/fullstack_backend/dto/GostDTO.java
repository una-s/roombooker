package com.roombooker.fullstack_backend.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.roombooker.fullstack_backend.model.Grad;

//oznacaven kao DTO objekat
public class GostDTO implements Serializable, DomainDTO {

	//svi atributi odgovaraju atributima u klasi Gost
	private int idGost;
	private String ime;
	private String prezime;
	private String email;
	private String telefon;
	private String adresa;
	private LocalDate datumRodjenja;
	private String lozinka;
	private GradDTO grad;

	
	//konstruktori i getteri i setteri
	public GostDTO() {
	}

	public GostDTO(int idGost, String ime, String prezime, String email, String telefon, String adresa,
			LocalDate datumRodjenja, String lozinka, GradDTO grad) {
		this.idGost = idGost;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.telefon = telefon;
		this.adresa = adresa;
		this.datumRodjenja = datumRodjenja;
		this.lozinka=lozinka;
		this.grad=grad;
	}

	public GradDTO getGrad() {
		return grad;
	}

	public void setGrad(GradDTO grad) {
		this.grad = grad;
	}

	public int getIdGost() {
		return idGost;
	}

	public void setIdGost(int idGost) {
		this.idGost = idGost;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}		

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	@Override
	public String toString() {
		return "GostDTO{" + "idGost=" + idGost + ", ime='" + ime + '\'' + ", prezime='" + prezime + '\'' + ", email='"
				+ email + '\'' + ", telefon='" + telefon + '\'' + ", adresa='" + adresa + '\'' + ", datumRodjenja="
				+ datumRodjenja + '}';
	}
}
