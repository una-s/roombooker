package com.roombooker.fullstack_backend.service;

import com.roombooker.fullstack_backend.JPARepo.GradRepository;
import com.roombooker.fullstack_backend.dto.GradDTO;
import com.roombooker.fullstack_backend.mapper.GradMapper;
import com.roombooker.fullstack_backend.model.Grad;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradService {

    private final GradRepository gradRepository;
    private final GradMapper gradMapper;

    public GradService(GradRepository gradRepository, GradMapper gradMapper) {
        this.gradRepository = gradRepository;
        this.gradMapper = gradMapper;
    }

    public List<GradDTO> getAll() {
        List<Grad> gradovi = gradRepository.findAll();
        return gradovi.stream()
                .map(gradMapper::toDomainDTO)
                .collect(Collectors.toList());
    }
}
