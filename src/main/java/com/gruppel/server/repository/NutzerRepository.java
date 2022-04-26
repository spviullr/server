package com.gruppel.server.repository;

import com.gruppel.server.entity.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutzerRepository extends JpaRepository<Nutzer, Integer> {
    Nutzer findByEmail(String email);
}
