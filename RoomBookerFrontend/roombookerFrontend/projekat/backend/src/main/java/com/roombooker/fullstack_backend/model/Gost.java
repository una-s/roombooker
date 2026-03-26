package com.roombooker.fullstack_backend.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity //klasa predstavlja entitet/tabelu u bazi
@Table(name = "gost") //u bazi se tabela zove gost
//omogucava bezbedni prenos podataka i oznacena kao domenski entitet
public class Gost implements Serializable, DomainEntity {

    @Id //PK koji se automatski generise
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //za definisanje osobina kolone u tabeli
    @Column(name = "id_gost", nullable = false, updatable = false)
    private int idGost; //jedinstveni identifikator svakog gosta u bazi

    @Column(name = "ime", nullable = false, updatable = true)
    private String ime;
    //vrednosti se mogu menjati nakon kreiranja updatabel=true
    @Column(name = "prezime", nullable = false, updatable = true)
    private String prezime;

    @Column(name = "telefon", nullable = true, updatable = true, unique=true)
    private String telefon;
    //telefon je jedinstven atribut svakog gosta

    @Column(name = "email", nullable = false, unique = true, updatable = true)
    private String email;
    //takodje jedinstven za potrebe registracije

    @Column(name = "adresa", nullable = true, updatable = true)
    private String adresa;

    @Column(name = "datum_rodjenja", nullable = false, updatable = true)
    private LocalDate datumRodjenja;

    @Column(name = "lozinka", nullable = false, updatable = true)
    private String lozinka;

    
    @ManyToOne
    @JoinColumn(name = "id_grad", nullable = false, updatable = true)
    private Grad grad;
    //definisemo vezu izmedju tabele gost i grad, vise gostiju (many)
    //moze pripadati jednom gradu (one). FK u tabeli gost je id_grad
    
    
    //konstruktori i setteri i getteri
    public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}

	public Gost() {
    }

    public Gost(int idGost, String ime, String prezime, String telefon, String email, 
                String adresa, LocalDate datumRodjenja, String lozinka, Grad grad) {
        this.idGost = idGost;
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.email = email;
        this.adresa = adresa;
        this.datumRodjenja = datumRodjenja;
        this.lozinka = lozinka;
        this.grad=grad;
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

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setLozinkaHash(String lozinka) {
        this.lozinka = lozinka;
    }

    @Override
    public String toString() {
        return "Gost{" +
                "idGost=" + idGost +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
