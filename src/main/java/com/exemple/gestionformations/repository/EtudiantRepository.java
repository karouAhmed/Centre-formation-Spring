package com.exemple.gestionformations.repository;

import com.exemple.gestionformations.entities.Etudiant;
import com.exemple.gestionformations.entities.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtudiantRepository  extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByUserEmail(String email);
}
