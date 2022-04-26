package com.gruppel.server.service;

import com.gruppel.server.entity.Nutzer;
import com.gruppel.server.repository.NutzerRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutzerService {

    @Autowired
    private NutzerRepository repository;

    public Nutzer createNutzer(Nutzer nutzer){
        nutzer.setPasswort(BCrypt.hashpw(nutzer.getPasswort(), BCrypt.gensalt()));

        // nutzer existiert bereits
        if(repository.findByEmail(nutzer.getEmail()) != null) return null;

        return repository.save(nutzer);
    }


    public boolean comparePassword(String password, Nutzer nutzer){
        return BCrypt.checkpw(password, nutzer.getPasswort());
    }

    public Nutzer getNutzerById(int id){
        return repository.findById(id).orElse(null);
    }

    public Nutzer getNutzerByEmail(String email){
        return repository.findByEmail(email);
    }

}
