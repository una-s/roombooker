package com.roombooker.fullstack_backend.mapper;

import com.roombooker.fullstack_backend.dto.DomainDTO;
import com.roombooker.fullstack_backend.model.DomainEntity;

public interface BaseMapper<DTO extends DomainDTO, DB extends DomainEntity> {

	public DTO toDomainDTO(DB DomainEntity);

	public DB toDomainEntity(DTO DomainDTO);

}
