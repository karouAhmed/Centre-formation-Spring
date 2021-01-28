package com.exemple.gestionformations.repository;

import com.exemple.gestionformations.entities.Etudiant;
import com.exemple.gestionformations.entities.Session;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session,Long> {

    List<Session> findAllByStatusIsNotLike(String status);
    List<Session> findAllByFormateurId(Long id);
}
