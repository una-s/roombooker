package com.roombooker.fullstack_backend.JPARepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roombooker.fullstack_backend.model.Soba;

public interface SobaRepository extends JpaRepository<Soba, Integer> {
	List<Soba> findAllByDostupnaTrue(); //SK3 pretraga slobodnih soba
	//za korisnicki interfejs da se prikazu sve dostupne sobe, a sk4 za proveru rezervacija
	//u buducnosti...
	
	Soba findByBrojSobe(Integer brojSobe); //za sk6 pre dodavanja sobe proveriti da li vec
	//postoji soba sa datim brojem
	
	//takodje za sk7 izmen podataka sobe prvo treba pronaci odg sobu
	
	void deleteById(Integer id); // za brisanje sobe
	void deleteByBrojSobe(Integer brojSobe); // ne mora mozda

}
