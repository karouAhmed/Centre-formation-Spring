package com.exemple.gestionformations.repository;

import com.exemple.gestionformations.entities.Formateur;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormateurRepository extends JpaRepository<Formateur,Long> {

    Optional<Formateur> findByUserEmail(String email);
}
