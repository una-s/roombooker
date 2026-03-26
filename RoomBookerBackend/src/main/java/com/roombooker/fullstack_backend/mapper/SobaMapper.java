package com.roombooker.fullstack_backend.mapper;

import org.springframework.stereotype.Component;

import com.roombooker.fullstack_backend.dto.SobaDTO;
import com.roombooker.fullstack_backend.model.Soba;
import com.roombooker.fullstack_backend.model.TipSobe;

@Component //sve klase koje imaju @AutoWired mogu da koriste ovu klasu jer se ona automatski instancira od strane Spring Boota
public class SobaMapper {

	private final TipSobeMapper tipSobeMapper;

	public SobaMapper(TipSobeMapper tipSobeMapper) {
		this.tipSobeMapper = tipSobeMapper;
	}

	public Soba toDomainEntity(SobaDTO sobaDTO, TipSobe tipSobe) {
		if (sobaDTO == null)
			return null;

		Soba soba = new Soba();
		if (sobaDTO.getIdSoba() != 0) { 
			soba.setIdSoba(sobaDTO.getIdSoba());
		}
		soba.setSprat(sobaDTO.getSprat());
		soba.setBrojSobe(sobaDTO.getBrojSobe());
		soba.setDostupna(sobaDTO.isDostupna());
		soba.setTipSobe(tipSobe);
		soba.setSlika(sobaDTO.getSlika());
		return soba;
	}

	public SobaDTO toDomainDTO(Soba soba) {
		if (soba == null)
			return null;

		SobaDTO sobaDTO = new SobaDTO();
		sobaDTO.setIdSoba(soba.getIdSoba());
		sobaDTO.setSprat(soba.getSprat());
		sobaDTO.setBrojSobe(soba.getBrojSobe());
		sobaDTO.setDostupna(soba.isDostupna());

		if (soba.getTipSobe() != null) {
			sobaDTO.setTipSobe(tipSobeMapper.toDomainDTO(soba.getTipSobe()));
		}
		sobaDTO.setSlika(soba.getSlika());
		return sobaDTO;
	}
}
