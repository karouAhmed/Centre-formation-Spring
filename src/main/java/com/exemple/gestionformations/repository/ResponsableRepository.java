package com.exemple.gestionformations.repository;

import com.exemple.gestionformations.entities.Formateur;
import com.exemple.gestionformations.entities.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsableRepository  extends JpaRepository<Responsable,Long> {
}
