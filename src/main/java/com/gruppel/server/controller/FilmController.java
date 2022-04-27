package com.gruppel.server.controller;

import com.gruppel.server.entity.Film;
import com.gruppel.server.service.FilmService;
import com.gruppel.server.service.ScrapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FilmController {

    @Autowired
    public FilmService service;

    @Autowired
    public ScrapeService scrapeService;

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

    @GetMapping("/scrapeFilme/{yearMin}/{yearMax}/{genre}/{maxAmount}")
    public ResponseEntity get_scrapeFilme(@PathVariable String yearMin, @PathVariable String yearMax, @PathVariable String genre, @PathVariable int maxAmount){
        List<Film> newFilms = scrapeService.scrapeIMDB(yearMin, yearMax, genre, maxAmount);

        System.out.println("added: " + newFilms.size());
        service.createFilms(newFilms);

        return new ResponseEntity<>(newFilms, HttpStatus.OK);
    }

    @PostMapping ("/scrapeFilme/{yearMin}/{yearMax}/{genre}/{maxAmount}")
    public ResponseEntity post_scrapeFilme(@PathVariable String yearMin, @PathVariable String yearMax, @PathVariable String genre, @PathVariable int maxAmount){
        List<Film> newFilms = scrapeService.scrapeIMDB(yearMin, yearMax, genre, maxAmount);

        service.createFilms(newFilms);

        return new ResponseEntity<>(newFilms, HttpStatus.OK);
    }
}
