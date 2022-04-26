package com.gruppel.server.controller;

import com.gruppel.server.entity.Nutzer;
import com.gruppel.server.service.NutzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NutzerController {

    @Autowired
    private NutzerService service;

    @PostMapping("/createNutzer")
    public ResponseEntity createNutzer(@RequestBody Nutzer nutzer){
        Nutzer neuerNutzer = service.createNutzer(nutzer);

        // nutzer existiert bereits
        if(neuerNutzer == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>(neuerNutzer, HttpStatus.OK);
    }

    
   /* @GetMapping("/getNutzerByEmail/{email}")
    public Nutzer getNutzerByEmail(@PathVariable String email){
        return service.getNutzerByEmail(email);
    }*/
}
