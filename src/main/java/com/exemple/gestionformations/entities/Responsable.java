package com.exemple.gestionformations.entities;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
public class Responsable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "numTlf")
    private String numTlf;

    @OneToOne(cascade = CascadeType.ALL)
    User user;
}
