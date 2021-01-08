package com.exemple.gestionformations.entities;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    User user;
}
