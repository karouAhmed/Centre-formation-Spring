package com.exemple.gestionformations.repository;

import com.exemple.gestionformations.entities.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormateurRepository extends JpaRepository<Formateur,Long> {
}
