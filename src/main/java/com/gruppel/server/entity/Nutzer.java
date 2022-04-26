package com.gruppel.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

//@Data -> keine getter/setter weil lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nutzer")
public class Nutzer {

    @Id
    @GeneratedValue
    private int id;
    private String email;
    private String vorname;
    private String nachname;
    private String passwort;
    // as unix timestamp, z.B. new Date().getTime()
    private long geburtsdatum;
    private boolean admin;
}
