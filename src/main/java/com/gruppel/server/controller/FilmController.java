package com.gruppel.server.controller;

import com.gruppel.server.entity.Film;
import com.gruppel.server.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FilmController {

    @Autowired
    public FilmService service;

    @PostMapping("/createFilm")
    public ResponseEntity createFilm(@RequestBody Film film){
        Film neuerFilm = service.createFilm(film);

        // nutzer existiert bereits
        if(neuerFilm == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>(neuerFilm, HttpStatus.OK);
    }

    @PostMapping("/createFilms")
    public ResponseEntity createFilms(@RequestBody List<Film> films){
        return new ResponseEntity<>(service.createFilms(films), HttpStatus.OK);
    }
}
