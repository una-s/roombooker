package com.roombooker.fullstack_backend.service;

// Importi su izostavljeni prema zahtevu

import com.roombooker.fullstack_backend.JPARepo.SobaRepository;
import com.roombooker.fullstack_backend.JPARepo.TipSobeRepository;
import com.roombooker.fullstack_backend.dto.SobaDTO;
import com.roombooker.fullstack_backend.mapper.SobaMapper;
import com.roombooker.fullstack_backend.model.Soba;
import com.roombooker.fullstack_backend.model.TipSobe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Annotation @Service označava ovu klasu kao Spring Service komponentu.
// To znači da Spring može da detektuje ovu klasu, kreira instancu i injektuje je tamo gde je potrebna.
@Service
// Annotation @Transactional obezbeđuje da se sve metode u ovoj klasi izvršavaju unutar transakcije.
// To znači da ako se desi greška unutar metode, sve promene u bazi podataka će biti automatski poništene (rollback).
@Transactional
public class SobaService {

	// Finalna polja za repozitorijume i mapper, biće injektovani preko konstruktora.
	private final SobaRepository sobaRepository;
	private final SobaMapper sobaMapper;
	private final TipSobeRepository tipSobeRepository;

	// Konstruktor za injekciju zavisnosti. Spring automatski injektuje potrebne instance.
	public SobaService(SobaRepository sobaRepository, SobaMapper sobaMapper, TipSobeRepository tipSobeRepository) {
		this.sobaRepository = sobaRepository;
		this.sobaMapper = sobaMapper;
		this.tipSobeRepository = tipSobeRepository;
	}

	// SK3 – Pretraga svih slobodnih soba
	// Metoda za dohvat svih soba koje su dostupne (dostupna = true).
	public List<SobaDTO> getAvailableRooms() {
		// Poziva metodu iz SobaRepository koja vraća listu Soba entiteta gde je 'dostupna' true.
		List<Soba> availableRooms = sobaRepository.findAllByDostupnaTrue();
		// Koristi Stream API da mapira listu Soba entiteta u listu SobaDTO objekata
		// pomoću sobaMapper::toDomainDTO metode i sakuplja ih u novu listu.
		return availableRooms.stream().map(sobaMapper::toDomainDTO).collect(Collectors.toList());
	}

	// Dohvatanje svih soba (može koristiti recepcioner)
	// Metoda za dohvat svih soba bez obzira na njihovu dostupnost.
	public List<SobaDTO> getAllRooms() {
		// Poziva metodu iz SobaRepository koja vraća listu svih Soba entiteta.
		List<Soba> rooms = sobaRepository.findAll();
		// Mapira listu Soba entiteta u listu SobaDTO objekata.
		return rooms.stream().map(sobaMapper::toDomainDTO).collect(Collectors.toList());
	}

	// SK6 – Dodavanje nove sobe
	// Metoda za dodavanje nove sobe u sistem. Prima SobaDTO objekat.
	public String addRoom(SobaDTO sobaDTO) {
		try {
			// 1. Provera da li soba sa istim brojem već postoji u bazi.
			Soba existingRoom = sobaRepository.findByBrojSobe(Integer.valueOf(sobaDTO.getBrojSobe()));
			if (existingRoom != null) {
				// Ako soba sa istim brojem već postoji, vraća poruku o grešci.
				return "Soba sa brojem " + sobaDTO.getBrojSobe() + " već postoji!";
			}

			// 2. Učitava TipSobe entitet iz baze na osnovu ID-ja koji se nalazi u SobaDTO.
			// Ako tip sobe nije pronađen, baca RuntimeException.
			TipSobe tipSobe = tipSobeRepository.findById(sobaDTO.getTipSobe().getIdTipSobe())
					.orElseThrow(() -> new RuntimeException(
							"Tip sobe nije pronađen sa ID-jem: " + sobaDTO.getTipSobe().getIdTipSobe()));

			// 3. Mapira SobaDTO objekat u Soba entitet, uključujući i pronađeni TipSobe.
			Soba soba = sobaMapper.toDomainEntity(sobaDTO, tipSobe);
			// Čuva (persisira) novi Soba entitet u bazu podataka.
			sobaRepository.save(soba);
			return "Soba je uspešno dodata!"; // Vraća poruku o uspehu.

		} catch (Exception ex) {
			// Hvata sve izuzetke koji se mogu desiti tokom operacije.
			ex.printStackTrace(); // Ispis steka grešaka u konzolu.
			return "Greška prilikom dodavanja sobe: " + ex.getMessage(); // Vraća poruku o grešci.
		}
	}

	// SK7 – Izmena podataka sobe
	// Metoda za izmenu postojećih podataka o sobi. Prima SobaDTO objekat.
	public String updateRoom(SobaDTO sobaDTO) {
		try {
			// 1. Provera da li soba sa datim ID-jem postoji u bazi.
			// Ako ne postoji, baca RuntimeException.
			sobaRepository.findById(sobaDTO.getIdSoba())
					.orElseThrow(() -> new RuntimeException("Soba nije pronađena sa ID-jem: " + sobaDTO.getIdSoba()));

			// 2. Učitava TipSobe entitet iz baze, slično kao kod dodavanja.
			TipSobe tipSobe = tipSobeRepository.findById(sobaDTO.getTipSobe().getIdTipSobe())
					.orElseThrow(() -> new RuntimeException(
							"Tip sobe nije pronađen sa ID-jem: " + sobaDTO.getTipSobe().getIdTipSobe()));

			// 3. Mapira SobaDTO u Soba entitet.
			// Važno je da DTO sadrži ID postojeće sobe da bi JpaRepository znao da je to update, a ne insert.
			Soba soba = sobaMapper.toDomainEntity(sobaDTO, tipSobe);
			// Čuva (ažurira) Soba entitet u bazu podataka.
			sobaRepository.save(soba);
			return "Podaci o sobi su uspešno izmenjeni!"; // Vraća poruku o uspehu.

		} catch (Exception ex) {
			// Hvata sve izuzetke.
			ex.printStackTrace();
			return "Greška prilikom izmene sobe: " + ex.getMessage();
		}
	}

	// SK8 – Brisanje sobe
	// Metoda za brisanje sobe po njenom ID-ju.
	public String deleteRoom(int idSoba) {
		try {
			// Proverava da li soba sa datim ID-jem postoji.
			if (sobaRepository.existsById(idSoba)) {
				// Ako soba postoji, briše je iz baze.
				sobaRepository.deleteById(idSoba);
				return "Soba je uspešno obrisana!"; // Vraća poruku o uspehu.
			} else {
				// Ako soba ne postoji, vraća odgovarajuću poruku.
				return "Soba sa ID-jem " + idSoba + " nije pronađena.";
			}
		} catch (Exception ex) {
			// Hvata sve izuzetke.
			ex.printStackTrace();
			return "Greška prilikom brisanja sobe: " + ex.getMessage();
		}
	}

	// Dodatno – Pronalazak sobe po ID-ju (korisno za SK7 ili prikaz detalja)
	// Metoda za dohvat jedne sobe na osnovu njenog ID-ja.
	public SobaDTO getRoomById(int idSoba) {
		// Pokušava pronaći sobu po ID-ju. Ako je ne pronađe, vraća null.
		Soba soba = sobaRepository.findById(idSoba).orElse(null);
		// Mapira pronađeni Soba entitet u SobaDTO objekat. Ako je soba null, mapper će se pobrinuti za to.
		return sobaMapper.toDomainDTO(soba);
	}
}