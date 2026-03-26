
package com.roombooker.fullstack_backend.mapper;

import com.roombooker.fullstack_backend.dto.TipSobeDTO;
import com.roombooker.fullstack_backend.model.TipSobe;
import org.springframework.stereotype.Component;

@Component
public class TipSobeMapper {

	public TipSobeDTO toDomainDTO(TipSobe entity) {
		if (entity == null)
			return null;

		TipSobeDTO dto = new TipSobeDTO();
		dto.setIdTipSobe(entity.getIdTipSobe());
		dto.setNaziv(entity.getNaziv());
		dto.setOpis(entity.getOpis());
		dto.setCenaPoNoci(entity.getCenaPoNoci());

		return dto;
	}

	public TipSobe toDomainEntity(TipSobeDTO dto) {
		if (dto == null)
			return null;

		TipSobe entity = new TipSobe();

		// ID se obično ne postavlja ručno osim kod update-a
		if (dto.getIdTipSobe() != 0) {
			entity.setIdTipSobe(dto.getIdTipSobe());
		}

		entity.setNaziv(dto.getNaziv());
		entity.setOpis(dto.getOpis());
		entity.setCenaPoNoci(dto.getCenaPoNoci());

		return entity;
	}
}
