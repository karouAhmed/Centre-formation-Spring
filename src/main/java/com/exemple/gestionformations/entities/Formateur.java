package com.exemple.gestionformations.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Data
@Entity
public class Formateur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "specialite")
    private String specialite;
    @OneToOne(cascade = CascadeType.ALL)
    User user;
    @OneToMany
    List<Session> sessions;
}
