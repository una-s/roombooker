package com.roombooker.fullstack_backend.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Grad implements Serializable, DomainEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grad", nullable = false, updatable = false, unique = true)
    private int idGrad;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "postanski_broj", nullable = false)
    private String postanskiBroj;

    public Grad() {
    }

    public Grad(int idGrad, String naziv, String postanskiBroj) {
        this.idGrad = idGrad;
        this.naziv = naziv;
        this.postanskiBroj = postanskiBroj;
    }

    public int getIdGrad() {
        return idGrad;
    }

    public void setIdGrad(int idGrad) {
        this.idGrad = idGrad;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }

    @Override
    public String toString() {
        return "Grad{" +
                "idGrad=" + idGrad +
                ", naziv='" + naziv + '\'' +
                ", postanskiBroj='" + postanskiBroj + '\'' +
                '}';
    }
}
