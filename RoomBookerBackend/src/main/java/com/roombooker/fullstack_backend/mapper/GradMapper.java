package com.roombooker.fullstack_backend.mapper;

import com.roombooker.fullstack_backend.dto.GradDTO;
import com.roombooker.fullstack_backend.model.Grad;
import org.springframework.stereotype.Component;

@Component
public class GradMapper implements BaseMapper<GradDTO, Grad> {

    @Override
    public GradDTO toDomainDTO(Grad domainEntity) {
        GradDTO gradDTO = new GradDTO();
        gradDTO.setId(domainEntity.getIdGrad());
        gradDTO.setName(domainEntity.getNaziv());
        gradDTO.setPostanskiBroj(domainEntity.getPostanskiBroj());
        return gradDTO;
    }

    @Override
    public Grad toDomainEntity(GradDTO domainDTO) {
        Grad grad = new Grad();
        grad.setIdGrad(domainDTO.getId());
        grad.setNaziv(domainDTO.getName());
        grad.setPostanskiBroj(domainDTO.getPostanskiBroj());
        return grad;
    }
}
