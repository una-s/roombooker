package com.roombooker.fullstack_backend.JPARepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roombooker.fullstack_backend.model.Gost;
import com.roombooker.fullstack_backend.model.Rezervacija;
import com.roombooker.fullstack_backend.model.Soba;
import com.roombooker.fullstack_backend.model.StatusRezervacije;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Integer> {
	List<Rezervacija> findByGost(Gost gost); //ako mi treba da vidim rezervacije za konkretnog gosta
	//SK13 - potvrda rezervacije mzd
	
	List<Rezervacija> findBySoba(Soba soba); // za odredjenu sobu da proverim jel ima rezrevacije
	//SK4 pre nego sto rezervisemo sobu proverimo koje rezervacije ima i jel je moguce rezervisati je
	List<Rezervacija> findByStatusRezervacije(StatusRezervacije status);;//mzd za filtriranje rezervacija
	
	void deleteById(Integer id); // po ID-u rezervacije
	void deleteBySoba(Soba soba); // opcionalno
	void deleteByGost(Gost gost); // opcionalno
	//sk14 brisanje rezervacije
}
