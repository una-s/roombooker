package com.roombooker.fullstack_backend.JPARepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roombooker.fullstack_backend.model.Grad;

public interface GradRepository extends JpaRepository<Grad, Integer> {

}
