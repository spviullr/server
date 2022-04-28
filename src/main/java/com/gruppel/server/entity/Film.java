package com.gruppel.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "filme")
public class Film {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String kategorie;
    private String laenge;
    private String erscheinungsdatum;
    private String regisseur;
    private String drehbuchautor;
    private String cast;
    private String filmbanner;

    public Film(String name, String kategorie, String laenge, String erscheinungsdatum, String regisseur, String drehbuchautor, String cast, String filmbanner) {
        this.name = name;
        this.kategorie = kategorie;
        this.laenge = laenge;
        this.erscheinungsdatum = erscheinungsdatum;
        this.regisseur = regisseur;
        this.drehbuchautor = drehbuchautor;
        this.cast = cast;
        this.filmbanner = filmbanner;
    }

}
