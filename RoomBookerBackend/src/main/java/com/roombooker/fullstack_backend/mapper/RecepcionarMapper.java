package com.roombooker.fullstack_backend.mapper;

import com.roombooker.fullstack_backend.dto.RecepcionarDTO;
import com.roombooker.fullstack_backend.model.Recepcionar;
import org.springframework.stereotype.Component;

@Component
public class RecepcionarMapper implements BaseMapper<RecepcionarDTO, Recepcionar> {

    @Override
    public RecepcionarDTO toDomainDTO(Recepcionar domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        RecepcionarDTO dto = new RecepcionarDTO();
        dto.setIdRecepcionar(domainEntity.getIdRecepcionar());
        dto.setIme(domainEntity.getIme());
        dto.setPrezime(domainEntity.getPrezime());
        dto.setEmail(domainEntity.getEmail());
        dto.setKorisnickoIme(domainEntity.getKorisnickoIme());
        dto.setLozinkaHash(domainEntity.getLozinkaHash());
        dto.setTelefon(domainEntity.getTelefon());

        return dto;
    }

    @Override
    public Recepcionar toDomainEntity(RecepcionarDTO domainDTO) {
        if (domainDTO == null) {
            return null;
        }

        Recepcionar entity = new Recepcionar();
        entity.setIdRecepcionar(domainDTO.getIdRecepcionar());
        entity.setIme(domainDTO.getIme());
        entity.setPrezime(domainDTO.getPrezime());
        entity.setEmail(domainDTO.getEmail());
        entity.setKorisnickoIme(domainDTO.getKorisnickoIme());
        entity.setLozinkaHash(domainDTO.getLozinkaHash());
        entity.setTelefon(domainDTO.getTelefon());

        return entity;
    }
}

