package com.roombooker.fullstack_backend.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Recepcionar implements Serializable, DomainEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_recepcionar", nullable = false, updatable = false, unique = true)
	private int idRecepcionar;

	@Column(name = "ime", nullable = false)
	private String ime;

	@Column(name = "prezime", nullable = false)
	private String prezime;

	@Column(name = "email", nullable = false, unique = true) 
	private String email;

	@Column(name = "korisnicko_ime", nullable = false, unique = true) 
																					
	private String korisnickoIme;

	@Column(name = "lozinka_hash", nullable = false) 
	private String lozinkaHash;

	@Column(name = "telefon")
	private String telefon;

	public Recepcionar() {
	}

	public Recepcionar(int id, String ime, String prezime, String email, String korisnickoIme, String lozinkaHash,
			String telefon) {
		this.idRecepcionar=id;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.korisnickoIme = korisnickoIme;
		this.lozinkaHash = lozinkaHash;
		this.telefon = telefon;
	}

	// --- Getters and Setters ---

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
		return "Recepcionar{" + "idRecepcionar=" + idRecepcionar + ", ime='" + ime + '\'' + ", prezime='" + prezime
				+ '\'' + ", korisnickoIme='" + korisnickoIme + '\'' + '}';
	}

}
