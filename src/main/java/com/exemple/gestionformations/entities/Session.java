package com.exemple.gestionformations.entities;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "titre")
    private String titre;
    @Column(name = "nbHeures")
    private Long nbHeures;
    @Column(name = "status")
    private String status;
    @Column(name = "description")
    private String description;
    @Column(name = "dateDebut")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

   /* public void setDateDebut(String dateDebut) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(dateDebut, formatter);
        this.dateDebut = dateTime;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }*/

    @OneToOne
    Formateur formateur;
@ManyToMany
@JoinTable(name="session_etudiant",
        joinColumns=@JoinColumn(name="etudiant_id"),
        inverseJoinColumns=@JoinColumn(name="session_id"))
List<Etudiant>etudiants;
}
