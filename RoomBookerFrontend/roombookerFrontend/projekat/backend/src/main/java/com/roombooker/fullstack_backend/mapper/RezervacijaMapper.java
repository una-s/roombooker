package com.roombooker.fullstack_backend.mapper;

import com.roombooker.fullstack_backend.dto.RezervacijaDTO;
import com.roombooker.fullstack_backend.model.*;
import org.springframework.stereotype.Component;

@Component
public class RezervacijaMapper {

    // Injektujemo druge mappere
    private final SobaMapper sobaMapper;
    private final GostMapper gostMapper;
    private final RecepcionarMapper recepcionarMapper;

    public RezervacijaMapper(SobaMapper sobaMapper, GostMapper gostMapper, RecepcionarMapper recepcionarMapper) {
        this.sobaMapper = sobaMapper;
        this.gostMapper = gostMapper;
        this.recepcionarMapper = recepcionarMapper;
    }

    // DTO -> Entitet
    public Rezervacija toDomainEntity(RezervacijaDTO dto, Soba soba, Gost gost, Recepcionar recepcionar) {
        if (dto == null) {
            return null;
        }

        Rezervacija entity = new Rezervacija();

        if (dto.getIdRezervacija() != 0) {
            entity.setRezervacijaId(dto.getIdRezervacija());
        }

        entity.setDatumPrijave(dto.getDatumPrijave());
        entity.setDatumOdjave(dto.getDatumOdjave());
        entity.setUkupanIznos(dto.getUkupanIznos());
        entity.setDatumKreiranja(dto.getDatumKreiranja());
        entity.setBrojOsoba(dto.getBrojOsoba());

    
        try {
        	entity.setStatusRezervacije(dto.getStatusRezervacije());
        } catch (IllegalArgumentException e) {
            // Opcionalno: obrada ako string ne odgovara nijednom enumu
            throw new RuntimeException("Nepoznat status rezervacije: " + dto.getStatusRezervacije());
        }


        // Već pronađeni entiteti
        entity.setSoba(soba);
        entity.setGost(gost);
        entity.setRecepcionar(recepcionar);

        return entity;
    }

    // Entitet -> DTO
    public RezervacijaDTO toDomainDTO(Rezervacija entity) {
        if (entity == null) {
            return null;
        }

        RezervacijaDTO dto = new RezervacijaDTO();
        dto.setIdRezervacija(entity.getRezervacijaId());
        dto.setDatumPrijave(entity.getDatumPrijave());
        dto.setDatumOdjave(entity.getDatumOdjave());
        dto.setUkupanIznos(entity.getUkupanIznos());
        dto.setDatumKreiranja(entity.getDatumKreiranja());
        dto.setBrojOsoba(entity.getBrojOsoba());

        
        if (entity.getStatusRezervacije() != null) {
            dto.setStatusRezervacije(entity.getStatusRezervacije());
        }

        // Mapiranje povezanih objekata (ako želiš ih uključiti u DTO)
        // Inače, ovo možeš izostaviti ako u RezervacijaDTO nemaš polja za sobu/gosta/recepcionara
        // dto.setSoba(sobaMapper.toDomainDTO(entity.getSoba()));
        // dto.setGost(gostMapper.toDomainDTO(entity.getGost()));
        // dto.setRecepcionar(recepcionarMapper.toDomainDTO(entity.getRecepcionar()));
        // Koristimo injektovane mapere za umetnute objekte
        if (entity.getSoba() != null) {
        	dto.setSoba(sobaMapper.toDomainDTO(entity.getSoba()));
        }
        if (entity.getGost() != null) {
        	dto.setGost(gostMapper.toDomainDTO(entity.getGost()));
        }
        if (entity.getRecepcionar() != null) {
        	dto.setRecepcionar(recepcionarMapper.toDomainDTO(entity.getRecepcionar()));;
        }
        return dto;
    }
}

