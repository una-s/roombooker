package com.roombooker.fullstack_backend.JPARepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roombooker.fullstack_backend.model.Recepcionar;

public interface RecepcionarRepository extends JpaRepository<Recepcionar, Integer> {
	Recepcionar findByKorisnickoIme(String korisnickoIme);
	// Pronađi recepcionara po korisničkom imenu - login

    Recepcionar findByEmail(String email); //za login po emailu
    
    //oba zaa sk5
    boolean existsByEmail(String email); //da proverimo jel postoji u bazi za login
}
