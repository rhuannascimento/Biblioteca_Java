package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Tomador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TomadorRepository extends JpaRepository<Tomador, Long>{

}