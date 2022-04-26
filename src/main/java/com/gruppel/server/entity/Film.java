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
//{7WX>ZsBu7{}{JW)
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
}