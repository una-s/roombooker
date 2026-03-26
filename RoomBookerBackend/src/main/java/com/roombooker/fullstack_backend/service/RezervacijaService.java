package com.roombooker.fullstack_backend.service;

import com.roombooker.fullstack_backend.JPARepo.GostRepository;
import com.roombooker.fullstack_backend.JPARepo.RecepcionarRepository;
import com.roombooker.fullstack_backend.JPARepo.RezervacijaRepository;
import com.roombooker.fullstack_backend.JPARepo.SobaRepository;
import com.roombooker.fullstack_backend.dto.GostDTO;
import com.roombooker.fullstack_backend.dto.RezervacijaDTO;
import com.roombooker.fullstack_backend.mapper.GostMapper;
import com.roombooker.fullstack_backend.mapper.RecepcionarMapper;
import com.roombooker.fullstack_backend.mapper.RezervacijaMapper;
import com.roombooker.fullstack_backend.mapper.SobaMapper;
import com.roombooker.fullstack_backend.model.Gost;
import com.roombooker.fullstack_backend.model.Recepcionar;
import com.roombooker.fullstack_backend.model.Rezervacija;
import com.roombooker.fullstack_backend.model.Soba;
import com.roombooker.fullstack_backend.model.StatusRezervacije;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final RezervacijaMapper rezervacijaMapper;
    private final GostRepository gostRepository;
    private final SobaRepository sobaRepository;
    private final RecepcionarRepository recepcionarRepository;
    private final GostMapper gostMapper;
    private final SobaMapper sobaMapper;
    private final RecepcionarMapper recepcionarMapper;

    public RezervacijaService(RezervacijaRepository rezervacijaRepository,
                              RezervacijaMapper rezervacijaMapper,
                              GostRepository gostRepository,
                              SobaRepository sobaRepository,
                              RecepcionarRepository recepcionarRepository,
                              GostMapper gostMapper,
                              SobaMapper sobaMapper,
                              RecepcionarMapper recepcionarMapper) {
        this.rezervacijaRepository = rezervacijaRepository;
        this.rezervacijaMapper = rezervacijaMapper;
        this.gostRepository = gostRepository;
        this.sobaRepository = sobaRepository;
        this.recepcionarRepository = recepcionarRepository;
        this.gostMapper = gostMapper;
        this.sobaMapper = sobaMapper;
        this.recepcionarMapper = recepcionarMapper;
    }

    // SK4 + SK13: Kreiranje rezervacije sobe i potvrda rezervacije
    public String addRezervacija(RezervacijaDTO dto) {
        try {
            Rezervacija rezervacija = new Rezervacija();
            rezervacija.setDatumPrijave(dto.getDatumPrijave());
            rezervacija.setDatumOdjave(dto.getDatumOdjave());
            rezervacija.setBrojOsoba(dto.getBrojOsoba());
            rezervacija.setUkupanIznos(dto.getUkupanIznos());

            // Kada gost šalje zahtev, datum kreiranja je sada
            rezervacija.setDatumKreiranja(LocalDate.now());

            // Status PENDING jer gost tek šalje zahtev
            rezervacija.setStatusRezervacije(StatusRezervacije.PENDING);

            // Povezivanje postojecih entiteta iz baze
            sobaRepository.findById(dto.getSoba().getIdSoba())
                    .ifPresent(rezervacija::setSoba);

            gostRepository.findById(dto.getGost().getIdGost())
                    .ifPresent(rezervacija::setGost);

            // Recepcionar ostaje null dok ne potvrdi rezervaciju
            rezervacija.setRecepcionar(null);

            rezervacijaRepository.save(rezervacija);
            return "Rezervacija je uspesno kreirana!";
        } catch (Exception ex) {
            ex.printStackTrace(); // da vidiš detalje greške
            return "Rezervacija ne moze da se kreira!";
        }
    }



    // SK14: Brisanje rezervacije
    public String deleteRezervacija(int rezervacijaId) {
        try {
            rezervacijaRepository.deleteById(rezervacijaId);
            return "Rezervacija obrisana uspešno!";
        } catch (Exception ex) {
            return "Neuspešno brisanje rezervacije!";
        }
    }

    // SK4: Dohvatanje rezervacije po ID-u
    public RezervacijaDTO getRezervacija(int rezervacijaId) {
        Rezervacija rezervacija = rezervacijaRepository.findById(rezervacijaId).orElse(null);
        return rezervacijaMapper.toDomainDTO(rezervacija);
    }

    // SK4: Dohvatanje svih rezervacija
    public List<RezervacijaDTO> getAllRezervacije() {
        List<Rezervacija> lista = rezervacijaRepository.findAll();
        return lista.stream()
                .map(rezervacijaMapper::toDomainDTO)
                .collect(Collectors.toList());
    }

    // SK14: Dohvatanje rezervacija za određenog gosta (potrebno za potvrdu ili pregled)
    public List<RezervacijaDTO> getRezervacijeZaGosta(GostDTO gostDTO) {
        Gost gost = gostMapper.toDomainEntity(gostDTO);
        List<Rezervacija> lista = rezervacijaRepository.findByGost(gost);
        return lista.stream()
                .map(rezervacijaMapper::toDomainDTO)
                .collect(Collectors.toList());
    }

    // SK4: Dohvatanje rezervacija za odredjenu sobu (provera slobodnih termina)
    public List<RezervacijaDTO> getRezervacijeZaSobu(int idSoba) {
        Soba soba = sobaRepository.findById(idSoba).orElseThrow(() -> new RuntimeException("Soba not found."));
        List<Rezervacija> lista = rezervacijaRepository.findBySoba(soba);
        return lista.stream()
                .map(rezervacijaMapper::toDomainDTO)
                .collect(Collectors.toList());
    }
    //filtriranje rezervacija po statusu(confirmed, cancelled...)
    public List<RezervacijaDTO> getRezervacijaStatus(StatusRezervacije status) {
        List<Rezervacija> rezervacije= rezervacijaRepository.findByStatusRezervacije(status);
        return rezervacije.stream()
                .map(rezervacijaMapper::toDomainDTO) // Znacajno pojednostavljeno
                .collect(Collectors.toList());
}

    // SK4: Azuriranje rezervacije
    @Transactional
    public String updateRezervacija(RezervacijaDTO dto) {
        try {
            Rezervacija rezervacijaIzBaze = rezervacijaRepository.findById(dto.getIdRezervacija())
                    .orElseThrow(() -> new RuntimeException("Rezervacija nije pronadjena."));

            // Pronadji povezane entitete
            Soba soba = sobaRepository.findById(dto.getSoba().getIdSoba()).orElseThrow(() -> new RuntimeException("Soba nije pronadjena."));
            Gost gost = gostRepository.findById(dto.getGost().getIdGost()).orElseThrow(() -> new RuntimeException("Gost nije pronadjen."));
            Recepcionar recepcionar = null;
            if (dto.getRecepcionar() != null) {
                recepcionar = recepcionarRepository.findById(dto.getRecepcionar().getIdRecepcionar())
                        .orElseThrow(() -> new RuntimeException("Recepcionar nije pronadjen."));
            }

            // Azuriramo polja
            rezervacijaIzBaze.setDatumPrijave(dto.getDatumPrijave());
            rezervacijaIzBaze.setDatumOdjave(dto.getDatumOdjave());
            rezervacijaIzBaze.setBrojOsoba(dto.getBrojOsoba());
            rezervacijaIzBaze.setUkupanIznos(dto.getUkupanIznos());
            rezervacijaIzBaze.setStatusRezervacije(dto.getStatusRezervacije());
            rezervacijaIzBaze.setDatumKreiranja(dto.getDatumKreiranja());

            // Postavi povezane entitete
            rezervacijaIzBaze.setSoba(soba);
            rezervacijaIzBaze.setGost(gost);
            rezervacijaIzBaze.setRecepcionar(recepcionar);

            rezervacijaRepository.save(rezervacijaIzBaze);
            return "Rezervacija ažurirana uspešno!";
        } catch (Exception ex) {
            return "Neuspešno ažuriranje rezervacije!";
        }
    }
    
    @Transactional // ova metoda ce nam sluziti za kreiranje rezervacije od strane recepcionara
    public String saveByReceptionist(RezervacijaDTO dto) {
        try {
            Rezervacija rezervacija = new Rezervacija();
            rezervacija.setDatumPrijave(dto.getDatumPrijave());
            rezervacija.setDatumOdjave(dto.getDatumOdjave());
            rezervacija.setBrojOsoba(dto.getBrojOsoba());

            // Povezivanje postojeće sobe
            Soba soba = sobaRepository.findById(dto.getSoba().getIdSoba())
                    .orElseThrow(() -> new RuntimeException("Soba nije pronadjena."));
            rezervacija.setSoba(soba);

            // Povezivanje postojeće gosta
            Gost gost = gostRepository.findById(dto.getGost().getIdGost())
                    .orElseThrow(() -> new RuntimeException("Gost nije pronadjen."));
            rezervacija.setGost(gost);

         // Povezivanje recepcionara koji dodaje rezervaciju
            if (dto.getRecepcionar() != null) {
                rezervacija.setRecepcionar(
                    recepcionarRepository.findById(dto.getRecepcionar().getIdRecepcionar())
                        .orElseThrow(() -> new RuntimeException("Recepcionar nije pronadjen."))
                );
            }



            // Status rezervacije je odmah CONFIRMED jer recepcionar dodaje
            rezervacija.setStatusRezervacije(StatusRezervacije.CONFIRMED);

            // Datum kreiranja
            rezervacija.setDatumKreiranja(LocalDate.now());

            // Izračunavanje ukupan iznos
            long brojNoci = ChronoUnit.DAYS.between(dto.getDatumPrijave(), dto.getDatumOdjave());
            if (brojNoci <= 0) {
                return "Datum odjave mora biti posle datuma prijave!";
            }
            double ukupanIznos = soba.getTipSobe().getCenaPoNoci() * brojNoci * dto.getBrojOsoba();
            rezervacija.setUkupanIznos(ukupanIznos);

            rezervacijaRepository.save(rezervacija);
            return "Rezervacija uspešno dodata od strane recepcionara!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Došlo je do greške prilikom dodavanja rezervacije!";
        }
    }

}
