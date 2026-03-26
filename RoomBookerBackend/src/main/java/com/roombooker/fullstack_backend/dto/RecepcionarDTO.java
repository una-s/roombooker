package com.roombooker.fullstack_backend.dto;

import java.io.Serializable;

public class RecepcionarDTO implements DomainDTO, Serializable {

	private int idRecepcionar;
	private String ime;
	private String prezime;
	private String email;
	private String korisnickoIme;
	private String lozinkaHash;
	private String telefon;

	public RecepcionarDTO() {
	}

	public RecepcionarDTO(int idRecepcionar, String ime, String prezime, String email, String korisnickoIme,
			String lozinkaHash, String telefon) {
		this.idRecepcionar = idRecepcionar;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.korisnickoIme = korisnickoIme;
		this.lozinkaHash = lozinkaHash;
		this.telefon = telefon;
	}

	public int getIdRecepcionar() {
		return idRecepcionar;
	}

	public void setIdRecepcionar(int idRecepcionar) {
		this.idRecepcionar = idRecepcionar;
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

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinkaHash() {
		return lozinkaHash;
	}

	public void setLozinkaHash(String lozinkaHash) {
		this.lozinkaHash = lozinkaHash;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	@Override
	public String toString() {
		return "RecepcionarDTO{" + "idRecepcionar=" + idRecepcionar + ", ime='" + ime + '\'' + ", prezime='" + prezime
				+ '\'' + ", email='" + email + '\'' + ", korisnickoIme='" + korisnickoIme + '\'' + ", telefon='"
				+ telefon + '\'' + '}';
	}
}
