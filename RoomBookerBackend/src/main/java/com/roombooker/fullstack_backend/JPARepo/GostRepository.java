package com.roombooker.fullstack_backend.JPARepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.roombooker.fullstack_backend.model.Gost;

public interface GostRepository extends JpaRepository<Gost, Integer> {

    // Pronalazi sve goste čije ime sadrži dati string, case-insensitive
    List<Gost> findByImeContainingIgnoreCase(String ime);

    // Pronalazi sve goste čije prezime sadrži dati string, case-insensitive
    List<Gost> findByPrezimeContainingIgnoreCase(String prezime);

    // Pronalazi jednog gosta po jedinstvenom email-u
    // Koristi se za proveru postojanja, dodavanje i login
    Gost findByEmail(String email);

    // Vraća sve goste iz baze
    List<Gost> findAll();

    // Proverava da li gost sa datim email-om postoji
    boolean existsByEmail(String email);

    @Modifying //kada se menjaju podaci nad bazom
    @Transactional //sve sto se izvrsava metodom se desava u jednoj transakciji
    void deleteByEmail(String email);
}
