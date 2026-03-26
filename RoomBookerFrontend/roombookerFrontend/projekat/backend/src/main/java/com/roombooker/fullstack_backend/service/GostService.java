package com.roombooker.fullstack_backend.service;

import com.roombooker.fullstack_backend.JPARepo.GostRepository;
import com.roombooker.fullstack_backend.JPARepo.GradRepository;
import com.roombooker.fullstack_backend.dto.GostDTO;
import com.roombooker.fullstack_backend.mapper.GostMapper;
import com.roombooker.fullstack_backend.model.Gost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service  // Spring prepoznaje ovu klasu kao servisni bean
@Transactional // Svi javni metodi se izvršavaju u okviru transakcije
public class GostService {

	private final GostRepository gostRepository;
	private final GostMapper gostMapper;
	private final GradRepository gradRepository;

	// Konstruktor za dependency injection (Spring automatski ubacuje repozitorijume i mapper)
	public GostService(GostRepository gostRepository, GostMapper gostMapper, GradRepository gradRepository) {
		this.gostRepository = gostRepository;
		this.gostMapper = gostMapper;
		this.gradRepository = gradRepository;
	}

	// SK9: Pretraga gosta po imenu ili prezimenu (case-insensitive)
	public List<GostDTO> searchGuests(String imePrezime) {
		List<Gost> gosti = gostRepository.findByImeContainingIgnoreCase(imePrezime);
		if (gosti.isEmpty()) { // Ako nema rezultata po imenu, pretražujemo po prezimenu
			gosti = gostRepository.findByPrezimeContainingIgnoreCase(imePrezime);
		}
		// Mapiranje entiteta u DTO za frontend
		return gosti.stream().map(gostMapper::toDomainDTO).collect(Collectors.toList());
	}

	// SK10: Dodavanje gosta (registracija)
	public String addGuest(GostDTO gostDTO) {
		try {
			if (gostRepository.existsByEmail(gostDTO.getEmail())) { // Provera jedinstvenog email-a
				return "Gost sa ovim emailom već postoji!";
			}
			Gost gost = gostMapper.toDomainEntity(gostDTO); // DTO → entitet
			// Postavljanje povezanog grada
			gradRepository.findById(gostDTO.getGrad().getId()).ifPresent(gost::setGrad);
			gostRepository.save(gost); // Čuvanje u bazi
			return "Gost je uspešno dodat!";
		} catch (Exception e) {
			return "Došlo je do greške prilikom dodavanja gosta.";
		}
	}

	// SK11: Izmena podataka gosta
	public String updateGuest(GostDTO gostDTO) {
		try {
			Gost gost = gostMapper.toDomainEntity(gostDTO); // DTO → entitet
			gradRepository.findById(gostDTO.getGrad().getId()).ifPresent(gost::setGrad);
			gostRepository.save(gost); // Čuvanje izmenjenog entiteta
			return "Podaci o gostu su uspešno ažurirani!";
		} catch (Exception e) {
			return "Greška prilikom ažuriranja podataka o gostu.";
		}
	}

	// SK12: Brisanje gosta
	@Transactional // Važno: delete menja bazu, mora biti u transakciji
	public String deleteGuest(String email) {
		try {
			gostRepository.deleteByEmail(email); // Briše gosta po email-u
			return "Gost je uspešno obrisan!";
		} catch (Exception e) {
			return "Greška prilikom brisanja gosta!";
		}
	}

	// SK1: Prijava gosta (login)
	public String login(String email, String lozinka) {
		try {
			Gost gost = gostRepository.findByEmail(email);
			// Provera lozinke (trenutno poređenje plain text, kasnije može hash)
			if (gost != null && gost.getLozinka().equals(lozinka)) {
				return "Dobrodošli, " + gost.getIme() + " " + gost.getPrezime() + "!";
			} else {
				return "Pogrešan email ili lozinka!";
			}
		} catch (Exception e) {
			return "Greška prilikom prijave!";
		}
	}

	// SK2: Registracija gosta (može koristiti isti addGuest)
	public String registerGuest(GostDTO gostDTO) {
		return addGuest(gostDTO);
	}

	// Dodatno: Pregled svih gostiju
	public List<GostDTO> getAll() {
		List<Gost> gosti = gostRepository.findAll();
		return gosti.stream().map(gostMapper::toDomainDTO).collect(Collectors.toList());
	}

	// Dodatno: Dohvatanje pojedinačnog gosta po email-u
	public GostDTO getGuestByEmail(String email) {
		Gost gost = gostRepository.findByEmail(email);
		return gostMapper.toDomainDTO(gost);
	}
}
