package com.gruppel.server.repository;

import com.gruppel.server.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    Film findByName(String name);
    Film findByNameAndErscheinungsdatum(String name, String erscheinungsdatum);
}
