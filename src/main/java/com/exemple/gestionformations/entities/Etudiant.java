package com.exemple.gestionformations.entities;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "numTlf")
    private String numTlf;


    @OneToOne(cascade = CascadeType.ALL)
    User user;

    @ManyToMany
    @JoinTable(name="session_etudiant",
            joinColumns=@JoinColumn(name="session_id"),
            inverseJoinColumns=@JoinColumn(name="etudiant_id"))
    List<Session> sessions;
}
