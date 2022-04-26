package com.gruppel.server.service;

import com.gruppel.server.entity.Film;
import com.gruppel.server.repository.FilmRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class FilmService {

    @Autowired
    private FilmRepository repository;

    public Film createFilm(Film film){
        // film existiert bereits
        if(repository.findByNameAndErscheinungsdatum(film.getName(), film.getErscheinungsdatum()) != null) return null;
        return repository.save(film);
    }

    public List<Film> createFilms(List<Film> films){
        List<Film> toAdd = new ArrayList<>();

        //add only non exisiting^
        for (Film film : films) {
            if(repository.findByNameAndErscheinungsdatum(film.getName(), film.getErscheinungsdatum()) == null){
                toAdd.add(film);
            }
        }

        return repository.saveAll(toAdd);
    }

    public int deleteFilmById(int id){
        repository.deleteById(id);
        return id;
    }

    public Film updateFilm(Film film){
        Film existingFilm = repository.findById(film.getId()).orElse(null);
        existingFilm.setName(film.getName());
        existingFilm.setKategorie(film.getKategorie());
        existingFilm.setLaenge(film.getLaenge());
        existingFilm.setErscheinungsdatum(film.getErscheinungsdatum());
        existingFilm.setRegisseur(film.getRegisseur());
        existingFilm.setDrehbuchautor(film.getDrehbuchautor());
        existingFilm.setCast(film.getCast());
        existingFilm.setFilmbanner(film.getFilmbanner());
        return repository.save(existingFilm);
    }


    public List<Film> getFilms(){
        return repository.findAll();
    }

    public Film getFilmById(int id){
        return repository.findById(id).orElse(null);
    }

    public Film getFilmByName(String name){
        return repository.findByName(name);
    }


}
