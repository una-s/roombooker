package com.roombooker.fullstack_backend.dto;

import java.io.Serializable;

public class GradDTO implements Serializable , DomainDTO{

	private int id;
	private String name;
	private String postanskiBroj;

	public GradDTO() {
	    }
//	public GradDTO(int id) {
//		this.id=id;
//	}

	public GradDTO(int id, String name, String postanskiBroj) {
	        this.id = id;
	        this.name = name;
	        this.postanskiBroj=postanskiBroj;
	    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(String postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	@Override
	public String toString() {
		return "CityDTO{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}
