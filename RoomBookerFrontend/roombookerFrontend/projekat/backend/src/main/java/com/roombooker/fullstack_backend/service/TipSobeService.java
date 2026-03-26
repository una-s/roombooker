package com.roombooker.fullstack_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roombooker.fullstack_backend.JPARepo.TipSobeRepository;
import com.roombooker.fullstack_backend.dto.TipSobeDTO;
import com.roombooker.fullstack_backend.mapper.TipSobeMapper;
import com.roombooker.fullstack_backend.model.TipSobe;

import jakarta.transaction.Transactional;

@Service // da spring zna da je bean s poslovnom logikom, moze da ga implementira kasnije
@Transactional // da se sve operacije ove klase treniraju kao jedna transakcija
public class TipSobeService {

	private final TipSobeRepository tipSobeRepo;
	private final TipSobeMapper tipSobeMapper;

	public TipSobeService(TipSobeRepository tipSobeRepo, TipSobeMapper tipSobeMapper) {
		this.tipSobeRepo = tipSobeRepo;
		this.tipSobeMapper = tipSobeMapper;
	}

	public List<TipSobeDTO> getAll() {
		List<TipSobe> tipoviSoba = tipSobeRepo.findAll();
		return tipoviSoba.stream().map(tipSobeMapper::toDomainDTO).collect(Collectors.toList());
	}
}
